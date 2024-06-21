package com.classhacker.spring_jwt.resource;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.classhacker.spring_jwt.config.JwtUtil;
import com.classhacker.spring_jwt.models.Role;
import com.classhacker.spring_jwt.models.User;
import com.classhacker.spring_jwt.services.MyUserDetailsService;

@RestController
public class RootResource {

    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;
    
    @GetMapping("/healthcheck")
    public String healthcheck() {
        System.out.println("HealthCheck endpoint called...");
        return "OK\n";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) throws Exception {
        String token = null;
        try {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Creds", e);
        }
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
        token = jwtUtil.generateToken(userDetails);
        return token;
    }

    // @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'ROLE_CONSUMER', 'COMSUMER')")
    // @RolesAllowed(value = {"SELLER", "ROLE_CONSUMER"})
    @GetMapping("/hello") 
    public String hello() {
        return "hello\n";
    }
    
}
