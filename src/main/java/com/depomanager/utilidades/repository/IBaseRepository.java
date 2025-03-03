package com.depomanager.utilidades.repository;

import java.util.Optional;

public interface IBaseRepository<T, ID> {
	
	// Método personalizado para verificar si una entidad con un código ya existe
    boolean existsByCodigo(String codigo);
    
    // Método para buscar una entidad por su código
    Optional<T> findByCodigo(String codigo);

    // Verifica si el código ya existe en otro deposito que no sea el que se está actualizando
    boolean existsByCodigoAndIdNot(String codigo, ID id);
}