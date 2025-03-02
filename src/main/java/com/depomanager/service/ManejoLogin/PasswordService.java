package com.depomanager.service.ManejoLogin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
 private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
 
 public String encriptar(String password) {
	 return passwordEncoder.encode(password);
 }
 
 public boolean verificar(String rawPassword,String hashedPassword) {
	 return passwordEncoder.matches(rawPassword, hashedPassword);
 }
}
