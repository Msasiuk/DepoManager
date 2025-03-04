package com.depomanager.auth.controllers;


import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.depomanager.auth.dtos.LoginDTO;
import com.depomanager.auth.services.PasswordService;
import com.depomanager.jwt.JwtUtil;
import com.depomanager.usuario.Roles;
import com.depomanager.usuario.models.Usuario;
import com.depomanager.usuario.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/auth")
@Tag(name="Autenticacion",description = "Operaciones sobre autenticacion de usuario")
public class AuthController {


@Autowired
private UsuarioRepository userRepository;

@Autowired
private PasswordService passwordService;



@PostMapping("/register")
@Operation(summary ="Registro de usuario",description ="Registro de usuario en base de datos")
public ResponseEntity<String> register(@RequestBody LoginDTO loginDTO) {
	System.out.println("Datos recibidos: " + loginDTO.getAlias() + ", " + loginDTO.getPassword());//BORRAR JOACO
  
    if (userRepository.findByAlias(loginDTO.getAlias()).isPresent()) {
        return ResponseEntity.status(400).body("El usuario ya existe");
    }

   
    String hashedPassword = passwordService.encriptar(loginDTO.getPassword());

    //Register guarda en bd
    Usuario newUser = new Usuario();
    newUser.setAlias(loginDTO.getAlias());
    newUser.setContrasenia(hashedPassword);
    newUser.setRoles(new HashSet<>(Collections.singletonList(Roles.USER)));
    userRepository.save(newUser);

    return ResponseEntity.ok("Usuario registrado exitosamente");
}
   

    @PostMapping("/login")
    @Operation(summary ="Logeo de usuario",description ="Autenticacion(logger) de usuario en base de datos")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Intentando autenticar usuario: " + loginDTO.getAlias());

        var userOptional = userRepository.findByAlias(loginDTO.getAlias());

        if (userOptional.isEmpty() || !passwordService.verificar(loginDTO.getPassword(), userOptional.get().getContrasenia())) {
            System.out.println("Credenciales incorrectas");//BORRAR JOACO(PRUEBAS)
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        String token = JwtUtil.generarToken(userOptional.get());
        System.out.println("Token generado: " + token);//BORRAR JOACO(PRUEBAS)
        return ResponseEntity.ok(token);
    }



}
