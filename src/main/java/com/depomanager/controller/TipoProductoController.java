package com.depomanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.model.TipoProducto;
import com.depomanager.repository.ITipoProductoRepository;

@RestController
@RequestMapping("/api/tipo-productos")
public class TipoProductoController extends BaseController<TipoProducto, Long> {
	
	@Autowired
	private ITipoProductoRepository tipoProductoRepository;
	
	// Sobrescribimos el método create para validar el código antes de crear
    @Override
    @PostMapping
    public ResponseEntity<String> create(@RequestBody TipoProducto tipoProducto) {
        if (tipoProductoRepository.existsByCodigo(tipoProducto.getCodigo())) {
            return ResponseEntity.badRequest().body("El código del tipo de producto ya existe.");
        }
        tipoProductoRepository.save(tipoProducto);
        return ResponseEntity.ok("Tipo de producto creado exitosamente.");
    }
    
}
