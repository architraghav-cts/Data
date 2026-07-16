package com.genc.springsecurity.service;

import com.genc.springsecurity.model.User;
import com.genc.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;
//
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        UserDetails user = org.springframework.security.core.userdetails.User.builder()
//                .username("admin")
//                .password(encoder.encode("admin123"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    public User getUserFromUsername(String username) {
        return repository.findByUsernameAndIsActive(username, true).orElseThrow(() -> new RuntimeException("User Not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserFromUsername(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }

    public User saveUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setActive(user.isActive());
        return repository.save(newUser);
    }
}