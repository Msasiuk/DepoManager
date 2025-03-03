package com.depomanager.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import com.depomanager.auth.services.CustomUserDetailsService;

import java.io.IOException;
import java.util.List;



public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = ((HttpServletRequest) request).getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Eliminar el prefijo "Bearer"

            Claims claims = jwtUtil.validarToken(token);

            if (claims != null) {
                String username = claims.getSubject();
                List<String> roles = jwtUtil.getRolesFromToken(token);

                
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

           
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        
        chain.doFilter(request, response);
    }
}
