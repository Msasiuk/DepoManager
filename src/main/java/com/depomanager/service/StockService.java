package com.depomanager.service;

import com.depomanager.model.Deposito;
import com.depomanager.model.Producto;
import com.depomanager.model.Stock;
import com.depomanager.repository.IStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class StockService {

	@Autowired
    private IStockRepository stockRepository;

    public void actualizarStock(Long productoId, Long depositoId, int cantidad, LocalDate fechaVencimiento) {
        Optional<Stock> stockOpt;

        if (fechaVencimiento != null) {
            stockOpt = stockRepository.findByProductoIdAndDepositoIdAndFechaVencimiento(
                    productoId, depositoId, fechaVencimiento);
        } else {
            stockOpt = stockRepository.findByProductoIdAndDepositoId(productoId, depositoId);
        }

        Stock stock = stockOpt.orElse(new Stock());
        stock.setCantidad(stock.getCantidad() + cantidad);
        stock.setProducto(new Producto(productoId));  // Asegura que el producto esté asignado
        stock.setDeposito(new Deposito(depositoId));
        stock.setFechaVencimiento(fechaVencimiento);

        stockRepository.save(stock);
    }
}