package com.depomanager.movimiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.depomanager.movimiento.models.MovimientoStock;

public interface IMovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {

    // Consulta personalizada para forzar la carga de depósitos
    @Query("SELECT m FROM MovimientoStock m " +
           "LEFT JOIN FETCH m.depositoOrigen " +
           "LEFT JOIN FETCH m.depositoDestino")
    List<MovimientoStock> findAllWithDepositos();
}