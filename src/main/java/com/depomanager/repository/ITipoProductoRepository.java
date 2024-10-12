package com.depomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.model.TipoProducto;

@Repository
public interface ITipoProductoRepository extends JpaRepository<TipoProducto, Long> {
	// Método para verificar si ya existe un código registrado
    boolean existsByCodigo(String codigo);
}
