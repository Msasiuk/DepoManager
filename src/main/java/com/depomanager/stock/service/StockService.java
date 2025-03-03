package com.depomanager.stock.service;

import com.depomanager.deposito.models.Deposito;
import com.depomanager.producto.models.Producto;
import com.depomanager.stock.models.Stock;
import com.depomanager.stock.repository.IStockRepository;

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