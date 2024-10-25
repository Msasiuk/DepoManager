package com.depomanager.controller;

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
    protected boolean existeEntidadDuplicada(Producto producto) {
        return productoRepository.existsByCodigo(producto.getCodigo());
    }

    @Override
    protected void preGuardarEntidad(Producto producto) {
        fechaService.establecerFechasPorDefecto(producto);
    }
   
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody Producto productoDetails) {
        Map<String, String> response = new HashMap<>();

        return repository.findById(id).map(producto -> {
            // Verificar si el código pertenece a otro producto
            if (productoRepository.existsByCodigoAndIdNot(productoDetails.getCodigo(), id)) {
                response.put("message", "El código del producto ya existe en otro registro.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Actualizar los campos del producto
            producto.setCodigo(productoDetails.getCodigo());
            producto.setDescripcion(productoDetails.getDescripcion());
            producto.setTieneVencimiento(productoDetails.getTieneVencimiento());
            producto.setTipoProducto(productoDetails.getTipoProducto());
            producto.setStockMaximo(productoDetails.getStockMaximo());
            producto.setStockMinimo(productoDetails.getStockMinimo());
            producto.setPuntoReposicion(productoDetails.getPuntoReposicion());

            repository.save(producto);
            response.put("message", "Producto modificado exitosamente.");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Producto no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
    }
}