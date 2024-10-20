package com.depomanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.model.Producto;
import com.depomanager.repository.IProductoRepository;
import com.depomanager.service.FechaService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends BaseController<Producto, Long> {

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private FechaService fechaService;

    @Override
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody Producto producto) {
        // Validar si el código ya existe
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            // Devolver 409 Conflict si el código ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El código del producto ya existe."));
        }

        // Usar FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(producto);

        // Valores por defecto para stock
        if (producto.getStockMaximo() == 0) producto.setStockMaximo(0);
        if (producto.getStockMinimo() == 0) producto.setStockMinimo(0);

        productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Producto creado exitosamente."));
    }
}