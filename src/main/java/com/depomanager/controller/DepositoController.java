package com.depomanager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody Deposito deposito) {
        // Validar si el de ya existe
        if (depositoRepository.existsByCodigo(deposito.getCodigo())) {
            // Devolver 409 Conflict si el código ya existe
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El código del deposito ya existe."));
        }

        // Usar FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(deposito);

        depositoRepository.save(deposito);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Deposito creado exitosamente."));
    }

}