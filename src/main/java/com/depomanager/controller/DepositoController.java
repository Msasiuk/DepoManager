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
import com.depomanager.model.Deposito;

import com.depomanager.repository.IDepositoRepository;
import com.depomanager.service.FechaService;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController extends BaseController<Deposito, Long> {

    @Autowired
    private IDepositoRepository depositoRepository;
    
    @Autowired
    private FechaService fechaService;

    @Override
    protected boolean existeEntidadDuplicada(Deposito deposito) {
        return depositoRepository.existsByCodigo(deposito.getCodigo());
    }

    @Override
    protected void preGuardarEntidad(Deposito deposito) {
        fechaService.establecerFechasPorDefecto(deposito);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable Long id, @RequestBody Deposito depositoDetails) {
        Map<String, String> response = new HashMap<>();

        return repository.findById(id).map(deposito -> {
            // Verificar si el código pertenece a otro depósito
            if (depositoRepository.existsByCodigoAndIdNot(depositoDetails.getCodigo(), id)) {
                response.put("message", "El código del depósito ya existe en otro registro.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Actualizar los campos del depósito
            deposito.setDescripcion(depositoDetails.getDescripcion());
            deposito.setCodigo(depositoDetails.getCodigo());

            repository.save(deposito);
            response.put("message", "Depósito modificado exitosamente.");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("message", "Depósito no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
    }
}