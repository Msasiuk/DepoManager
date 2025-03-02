package com.depomanager.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.depomanager.jwt.JwtAuthenticationFilter;
import com.depomanager.jwt.JwtUtil;
import com.depomanager.service.ManejoLogin.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de seguridad HTTP
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF si no usas cookies
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**","/inicio/**").permitAll()  // Permitir acceso sin autenticación
                .requestMatchers("/api/user/**").hasRole("USER")  // Solo usuarios con rol USER
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Solo usuarios con rol ADMIN
                .anyRequest().authenticated()  // Cualquier otra petición requiere autenticación
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
