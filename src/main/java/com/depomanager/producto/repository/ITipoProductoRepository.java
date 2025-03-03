package com.depomanager.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.producto.models.TipoProducto;
import com.depomanager.utilidades.repository.IBaseRepository;

@Repository
public interface ITipoProductoRepository extends JpaRepository<TipoProducto, Long>, 
IBaseRepository<TipoProducto, Long> {}


