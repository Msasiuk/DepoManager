package com.depomanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<String> create(@RequestBody Deposito deposito) {
        if (depositoRepository.existsByCodigo(deposito.getCodigo())) {
            return ResponseEntity.badRequest().body("El código del depósito ya existe.");
        }

        // Usar el FechaService para establecer fechas por defecto
        fechaService.establecerFechasPorDefecto(deposito);

        depositoRepository.save(deposito);
        return ResponseEntity.ok("Depósito creado exitosamente.");
    }
}