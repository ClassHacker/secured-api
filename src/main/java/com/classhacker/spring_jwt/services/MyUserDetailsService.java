package com.classhacker.spring_jwt.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.classhacker.spring_jwt.models.Role;
import com.classhacker.spring_jwt.models.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<Role> roles = new HashSet<>();
        if (username.equalsIgnoreCase("consumer")) {
            roles.add(Role.CONSUMER);
            return new User("consumer", "123", roles);
        }
        if (username.equalsIgnoreCase("seller")) {
            roles.add(Role.SELLER);
            return new User("seller", "456", roles);
        }
        if (username.equalsIgnoreCase("admin")) {
            roles.add(Role.CONSUMER);
            roles.add(Role.SELLER);
            return new User("admin", "789", roles);
        }
        throw new UsernameNotFoundException("User ID not found");
    }
    
}
