package com.depomanager.controller;

import com.depomanager.model.Stock;
import com.depomanager.repository.IStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Autowired
    private IStockRepository stockRepository;

    @GetMapping
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/deposito/{depositoId}")
    public ResponseEntity<List<Stock>> getStockPorDeposito(@PathVariable Long depositoId) {
        List<Stock> stockList = stockRepository.findByDepositoId(depositoId);
        
        if (stockList == null || stockList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // Devolver array vacío
        }
        
        return ResponseEntity.ok(stockList);
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        Stock newStock = stockRepository.save(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        return stockRepository.findById(id).map(stock -> {
            stock.setCantidad(stockDetails.getCantidad());
            stock.setFechaVencimiento(stockDetails.getFechaVencimiento());
            stockRepository.save(stock);
            return ResponseEntity.ok(stock);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return ResponseEntity.ok("Stock eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock no encontrado");
        }
    }
}

