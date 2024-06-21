package com.classhacker.spring_jwt.config;

import java.io.IOException;
import java.security.Security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.classhacker.spring_jwt.services.MyUserDetailsService;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("JWT");
        String token = null;
        String username = null;
		// System.out.println(header);
		if(header != null && !header.isEmpty() && header.startsWith("Bearer")){
			token = header.substring(7);
            username = jwtUtil.loadUserNameFromToken(token);
		}
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = myUserDetailsService.loadUserByUsername(username);
            System.out.println(user);
            if (jwtUtil.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken upat = 
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            } else {
                System.out.println("Token validation failed");
            }
        }
        filterChain.doFilter(request, response);
	}
    
}
