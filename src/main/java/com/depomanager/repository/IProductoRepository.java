package com.depomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long>{
    // Método personalizado para validar si un producto con un código ya existe
    boolean existsByCodigo(String codigo);

}
