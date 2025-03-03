package com.depomanager.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.depomanager.producto.models.DetalleProducto;

public interface IDetalleProductoRepository extends JpaRepository<DetalleProducto, Long> {}