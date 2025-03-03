package com.depomanager.proveedor.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.proveedor.models.Proveedor;
import com.depomanager.proveedor.repository.IProveedorRepository;
import com.depomanager.utilidades.controllers.BaseController;
import com.depomanager.utilidades.service.FechaService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController extends BaseController<Proveedor, Long> {

    @Autowired
    private IProveedorRepository proveedorRepository;
    
    @Autowired
    private FechaService fechaService;

    @Override
    protected boolean existeEntidadDuplicada(Proveedor proveedor) {
        return proveedorRepository.existsByCuitCuil(proveedor.getCuitCuil());
    }

    @Override
    protected void preGuardarEntidad(Proveedor proveedor) {
        fechaService.establecerFechasPorDefecto(proveedor);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody Proveedor proveedorDetails) {
        Map<String, String> response = new HashMap<>();

        return repository.findById(id).map(proveedor -> {
            // Verificar si el CUIT/CUIL pertenece a otro proveedor
            if (proveedorRepository.existsByCuitCuilAndIdNot(proveedorDetails.getCuitCuil(), id)) {
                response.put("message", "El CUIT/CUIL del proveedor ya existe en otro registro.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Validar que las fechas no sean nulas
            if (proveedorDetails.getFechaInicio() == null || proveedorDetails.getFechaFin() == null) {
                response.put("message", "Las fechas no pueden ser nulas.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validar que la fecha de fin no sea anterior a la fecha de inicio
            if (proveedorDetails.getFechaFin().isBefore(proveedorDetails.getFechaInicio())) {
                response.put("message", "La fecha de finalización no puede ser anterior a la fecha de inicio.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Actualizar los campos del proveedor
            proveedor.setCuitCuil(proveedorDetails.getCuitCuil());
            proveedor.setNombre(proveedorDetails.getNombre());
            proveedor.setRazonSocial(proveedorDetails.getRazonSocial());
            proveedorDetails.setFechaFin(proveedorDetails.getFechaFin());

            repository.save(proveedor);
            response.put("message", "Proveedor modificado exitosamente.");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Proveedor no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
    }
}
