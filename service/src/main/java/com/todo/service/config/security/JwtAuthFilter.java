package com.todo.service.config.security;

import com.todo.core.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtAuthService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    /* TODO
    * 1. Get authorization header
    * 2. Check header is present or not
    * 3. Extract the token from the Header
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            // 1
            String authHeader = request.getHeader("Authorization");
            String token= null;
            String userName = null;
            // 2
            System.out.println("Auth header is : " + request.getHeader("Bearer "));

            if(authHeader !=null && authHeader.startsWith("Bearer ")) {
                // 3
                log.info("JWT token found");
                token = authHeader.substring(7);
                userName= jwtAuthService.getUserNameFromToken(token);
            }
            log.info("Token value is : "+token);
            // If user already authenticated then it should store in the security context
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // TODOis fetch from db
                UserEntity userDetails =this.customUserDetailsService.loadUserByUsername(userName);
                if(userDetails !=null) {
                    // Validate the token
                    log.info("Is user details is null :: {}", userDetails==null);
                    boolean isValid = jwtAuthService.validateToken(userName, userDetails, token);
                    log.info("Is valid token is : "+isValid);
                    if (isValid) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // If valid then need to add those in the security context holder
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }else{
                    log.info("Is user details is null value :: {}", userDetails==null);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            throw e;
        }

    }
}
