package com.depomanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/inicio")
public class InicioController {

	
	
	@GetMapping("/usuario")
	public String obtenerUsuario(@RequestHeader("Authorization") String token){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Nombre del usuario autenticado
        return username;  // Devuelves el nombre del usuario como respuesta
	}
}
