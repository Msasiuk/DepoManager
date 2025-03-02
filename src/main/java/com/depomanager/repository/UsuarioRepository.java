package com.depomanager.repository;

import com.depomanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByAlias(String alias);

}
