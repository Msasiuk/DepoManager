package com.depomanager.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.producto.models.Producto;
import com.depomanager.utilidades.repository.IBaseRepository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long>, 
IBaseRepository<Producto, Long> {}

