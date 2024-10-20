package com.depomanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.model.Proveedor;
import com.depomanager.repository.IProveedorRepository;
import com.depomanager.service.FechaService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController extends BaseController<Proveedor, Long> {

    @Autowired
    private IProveedorRepository proveedorRepository;
    
    @Autowired
    private FechaService fechaService;

    @Override
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody Proveedor proveedor) {
        // Validar si el de ya existe
        if (proveedorRepository.existsByCuitCuil(proveedor.getCuitCuil())) {
            // Devolver 409 Conflict si el código ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El Cuit/Cuil del proveedor ya existe."));
        }

        // Usar FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(proveedor);

        proveedorRepository.save(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Proveedor creado exitosamente."));
    }
}