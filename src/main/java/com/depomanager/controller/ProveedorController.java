package com.depomanager.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> create(@RequestBody Proveedor proveedor) {
        if (proveedorRepository.existsByCuitCuil(proveedor.getCodigo())) {
            return ResponseEntity.badRequest().body("El CUIT/CUIL del proveedor ya existe.");
        }

   	 	// Usar el FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(proveedor);

        proveedorRepository.save(proveedor);
        return ResponseEntity.ok("Proveedor creado exitosamente.");
    }
}