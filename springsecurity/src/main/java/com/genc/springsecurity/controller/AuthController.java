package com.genc.springsecurity.controller;

import com.genc.springsecurity.model.AuthRequest;
import com.genc.springsecurity.model.User;
import com.genc.springsecurity.service.AuthService;
import com.genc.springsecurity.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/check1")
    public String check1() {
        return "Server 1 is running";
    }

    @GetMapping("/check2")
    public String check2() {
        return "Server 2 is running";
    }

    @PostMapping("/save")
    public void saveUser(@RequestBody User user) {
        service.saveUser(user);
    }

    @GetMapping("/csrf")
    public CsrfToken token(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @PostMapping("/authenticated")
    public String authenticate(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                        authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        }
        return null;
    }
}