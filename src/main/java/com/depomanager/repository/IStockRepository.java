package com.depomanager.repository;

import com.depomanager.model.Stock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IStockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductoIdAndDepositoIdAndFechaVencimiento(
        Long productoId, Long depositoId, LocalDate fechaVencimiento);
    
    Optional<Stock> findByProductoIdAndDepositoId(Long productoId, Long depositoId);

	
	List<Stock> findByDepositoId(Long depositoId);
	
	 @Query("SELECT s FROM Stock s WHERE s.deposito.id = :depositoId AND s.producto.id = :productoId")
	    Optional<Stock> findByDepositoAndProducto(@Param("depositoId") Long depositoId, @Param("productoId") Long productoId);
	}