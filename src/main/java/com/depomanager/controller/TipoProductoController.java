package com.depomanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.depomanager.model.TipoProducto;
import com.depomanager.repository.ITipoProductoRepository;
import com.depomanager.service.FechaService;


@RestController
@RequestMapping("/api/tipos-producto")
public class TipoProductoController extends BaseController<TipoProducto, Long> {
	
	@Autowired
	private ITipoProductoRepository tipoProductoRepository;
	
	@Autowired
    private FechaService fechaService;
	
	@Override
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody TipoProducto tipoProducto) {
        // Validar si el de ya existe
        if (tipoProductoRepository.existsByCodigo(tipoProducto.getCodigo())) {
            // Devolver 409 Conflict si el código ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El código del tipo de producto ya existe."));
        }

        // Usar FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(tipoProducto);

        tipoProductoRepository.save(tipoProducto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Tipo producto creado exitosamente."));
    }
}
    
