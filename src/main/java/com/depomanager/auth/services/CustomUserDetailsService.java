package com.depomanager.auth.services;

import com.depomanager.usuario.models.Usuario;
import com.depomanager.usuario.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.User;  // Usar 'User' de Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
  

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String alias) throws UsernameNotFoundException {
        
        Usuario user = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + alias));

        // Convertir el usuario en un objeto de userDetails de spring security
        return User.builder()
                .username(user.getAlias())
                .password(user.getContrasenia())  
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))//Manejo de roles a String
                .build();
    }
}

