package com.depomanager.repository;

import com.depomanager.model.DetalleProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDetalleProductoRepository extends JpaRepository<DetalleProducto, Long> {}