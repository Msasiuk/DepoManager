package com.depomanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.model.Deposito;

@Repository
public interface IDepositoRepository extends JpaRepository<Deposito, Long> {
    
    // Método personalizado para verificar si un depósito con un código ya existe
    boolean existsByCodigo(String codigo);

    // Método para buscar un depósito por su código
    Optional<Deposito> findByCodigo(String codigo);
}