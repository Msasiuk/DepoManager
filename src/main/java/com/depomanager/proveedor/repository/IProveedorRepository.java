package com.depomanager.proveedor.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.proveedor.models.Proveedor;

@Repository
public interface IProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Verifica si ya existe un proveedor con el mismo CUIT/CUIL
    boolean existsByCuitCuil(String cuitCuil);

    // Busca un proveedor por su CUIT/CUIL
    Optional<Proveedor> findByCuitCuil(String cuitCuil);

	boolean existsByCuitCuilAndIdNot(String cuitCuil, Long id);
}