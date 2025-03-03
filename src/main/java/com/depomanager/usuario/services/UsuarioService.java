package com.depomanager.usuario.services;
import java.util.List;


import org.springframework.stereotype.Service;

import com.depomanager.usuario.models.Usuario;
import com.depomanager.usuario.repository.UsuarioRepository;



@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }
}