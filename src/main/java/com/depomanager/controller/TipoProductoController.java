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
	    protected boolean existeEntidadDuplicada(TipoProducto tipoProducto) {
	        return tipoProductoRepository.existsByCodigo(tipoProducto.getCodigo());
	    }

	   
	 @Override
	    protected void preGuardarEntidad(TipoProducto tipoProducto) {
	        fechaService.establecerFechasPorDefecto(tipoProducto);
	    }
	 
	 @PutMapping("/{id}")
	    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody TipoProducto tipoProductoDetails) {
	        Map<String, String> response = new HashMap<>();

	        return repository.findById(id).map(tipoProducto -> {
	            // Verificar si el código pertenece a otro tipo producto
	            if (tipoProductoRepository.existsByCodigoAndIdNot(tipoProductoDetails.getCodigo(), id)) {
	                response.put("message", "El código del tipo producto ya existe en otro registro.");
	                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	            }

	            // Actualizar los campos del tipo producto
	            tipoProducto.setDescripcion(tipoProductoDetails.getDescripcion());
	            tipoProducto.setCodigo(tipoProductoDetails.getCodigo());

	            repository.save(tipoProducto);
	            response.put("message", "Tipo producto modificado exitosamente.");
	            return ResponseEntity.ok(response);
	        }).orElseGet(() -> {
	            response.put("message", "Tipo producto no encontrado.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        });
	    }
}
    
