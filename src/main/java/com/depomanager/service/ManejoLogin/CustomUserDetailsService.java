package com.depomanager.service.ManejoLogin;

import com.depomanager.model.Usuario;
import com.depomanager.repository.UsuarioRepository;
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
        // Buscar al usuario en la base de datos por nombre (username)
        Usuario user = usuarioRepository.findByAlias(alias)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + alias));

        // Convertir el usuario a un objeto UserDetails de Spring Security
        return User.builder()
                .username(user.getAlias())
                .password(user.getContrasenia())  // Asegúrate de que la contraseña esté cifrada
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))  // Convertir roles a String[]
                .build();
    }
}

