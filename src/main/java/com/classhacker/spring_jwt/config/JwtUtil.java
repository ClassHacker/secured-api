package com.classhacker.spring_jwt.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.classhacker.spring_jwt.models.User;
import com.classhacker.spring_jwt.services.MyUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    
    private String SECRET_KEY = "secret-token";

    @Autowired
    MyUserDetailsService myUserDetailsService;

    public Claims getClaims(String token) {
        return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody();
    }

    public String loadUserNameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
    
    public Boolean validateToken(String token, UserDetails user) {
        Claims claims = getClaims(token);
        System.out.println(claims);
        return (user.getUsername().equalsIgnoreCase(claims.getSubject()) );
        // && claims.getExpiration().before(new Date()));
    }

    public String generateToken(UserDetails user) {
		// User user = (User) myUserDetailsService.loadUserByUsername(username);
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", user.getAuthorities());
		user.getAuthorities();
		String token = Jwts.builder()
				.setClaims(claims)              
				.setSubject(String.format("%s", user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
		System.out.println("Token: "+ token);
		return token;
	}
    
}
