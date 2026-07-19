package com.example.telematics_fleet_management.config;

import com.example.telematics_fleet_management.model.User;
import com.example.telematics_fleet_management.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ADMIN = "ADMIN";
    private static final String FLEET_MANAGER = "FLEET_MANAGER";
    private static final String DRIVER = "DRIVER";
    private static final String SERVICE_ENGINEER = "SERVICE_ENGINEER";
    private static final String SAFETY_OFFICER = "SAFETY_OFFICER";
    private static final String OPERATIONS_ANALYST = "OPERATIONS_ANALYST";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/access-denied").permitAll()
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole(ADMIN)
                        .requestMatchers("/fleet/**", "/api/fleet/**").hasAnyRole(ADMIN, FLEET_MANAGER)
                        .requestMatchers("/driver/**", "/api/driver/**").hasAnyRole(ADMIN, FLEET_MANAGER, DRIVER, SAFETY_OFFICER)
                        .requestMatchers("/service/**", "/api/service/**").hasAnyRole(ADMIN, FLEET_MANAGER, SERVICE_ENGINEER)
                        .requestMatchers("/safety/**", "/api/safety/**").hasAnyRole(ADMIN, SAFETY_OFFICER)
                        .requestMatchers("/analyst/**", "/api/fuel-logs", "/api/fuel-logs/**").hasAnyRole(ADMIN, FLEET_MANAGER, OPERATIONS_ANALYST)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(roleBasedSuccessHandler())
                        .failureUrl("/?error")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email);

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + email);
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(securityPassword(user.getPassword()))
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            if (hasAuthority(authentication, role(ADMIN))) {
                response.sendRedirect("/admin/dashboard");
            } else if (hasAuthority(authentication, role(FLEET_MANAGER))) {
                response.sendRedirect("/fleet/register-vehicle");
            } else if (hasAuthority(authentication, role(DRIVER))) {
                response.sendRedirect("/driver/dashboard");
            } else if (hasAuthority(authentication, role(SERVICE_ENGINEER))) {
                response.sendRedirect("/service/dashboard");
            } else if (hasAuthority(authentication, role(SAFETY_OFFICER))) {
                response.sendRedirect("/safety/dashboard");
            } else if (hasAuthority(authentication, role(OPERATIONS_ANALYST))) {
                response.sendRedirect("/analyst/dashboard");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    private String securityPassword(String password) {
        if (password != null && password.startsWith("{")) {
            return password;
        }
        return "{noop}" + password;
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> authority.equals(grantedAuthority.getAuthority()));
    }

    private String role(String role) {
        return ROLE_PREFIX + role;
    }
}