package com.depomanager.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.model.Producto;
import com.depomanager.repository.IProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController extends BaseController<Producto, Long> {

    @Autowired
    private IProductoRepository productoRepository;

 
    @Override
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Producto producto) {
    	if (productoRepository.existsByCodigo(producto.getCodigo())) {
            return ResponseEntity.badRequest().body("El código del producto ya existe.");
        }

        // Establecer la fecha de inicio como la fecha actual si no se proporciona
        if (producto.getFechaInicio() == null) {
            producto.setFechaInicio(new Date());
        }

        // Establecer fecha fin a 31/12/9999 si no se proporciona
        if (producto.getFechaFin() == null) {
            producto.setFechaFin(new Date(Long.MAX_VALUE));
        }

        // Valores por defecto
        if (producto.getCantidad() == 0) producto.setCantidad(0);
        if (producto.getStockMaximo() == 0) producto.setStockMaximo(0);
        if (producto.getStockMinimo() == 0) producto.setStockMinimo(0);

        productoRepository.save(producto);
        return ResponseEntity.ok("Producto creado exitosamente.");
    }
}