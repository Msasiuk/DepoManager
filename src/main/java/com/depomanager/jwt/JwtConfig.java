package com.depomanager.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
	
	@Bean
	 JwtUtil jwtUtil() {
		return new JwtUtil();
	}
}
