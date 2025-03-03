package com.depomanager.movimiento.controllers;

import com.depomanager.deposito.models.Deposito;
import com.depomanager.movimiento.models.MovimientoStock;
import com.depomanager.movimiento.models.MovimientoStock.TipoMovimiento;
import com.depomanager.movimiento.repository.IMovimientoStockRepository;
import com.depomanager.producto.models.DetalleProducto;
import com.depomanager.producto.models.Producto;
import com.depomanager.stock.models.Stock;
import com.depomanager.stock.repository.IStockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoStockController {

    @Autowired
    private IMovimientoStockRepository movimientoStockRepository;
    
    @Autowired
    private IStockRepository stockRepository;

 // Utiliza la consulta personalizada para incluir los depósitos
    @GetMapping
    public List<MovimientoStock> getAllMovimientos() {
        return movimientoStockRepository.findAllWithDepositos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStock> getMovimientoById(@PathVariable Long id) {
        Optional<MovimientoStock> movimiento = movimientoStockRepository.findById(id);
        return movimiento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovimientoStock> createMovimiento(@RequestBody MovimientoStock movimiento) {
    	if (movimiento.getTipoMovimiento() == TipoMovimiento.INGRESO) {
            movimiento.setDepositoOrigen(null);
        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.EGRESO) {
            movimiento.setDepositoDestino(null);
            movimiento.setProveedor(null);
        }
        // Guardar el movimiento de stock
        MovimientoStock newMovimiento = movimientoStockRepository.save(movimiento);

        // Actualizar el stock según el tipo de movimiento
        actualizarStock(movimiento);

        return ResponseEntity.status(HttpStatus.CREATED).body(newMovimiento);
    }

    private void actualizarStock(MovimientoStock movimiento) {
        List<DetalleProducto> detalles = movimiento.getDetalles();

        for (DetalleProducto detalle : detalles) {
            int cantidad = detalle.getCantidad();
            Producto producto = detalle.getProducto();

            if (movimiento.getTipoMovimiento() == MovimientoStock.TipoMovimiento.INGRESO) {
                actualizarStockDestino(movimiento.getDepositoDestino(), producto, cantidad);
            } else if (movimiento.getTipoMovimiento() == MovimientoStock.TipoMovimiento.EGRESO) {
                actualizarStockOrigen(movimiento.getDepositoOrigen(), producto, -cantidad);
            } else if (movimiento.getTipoMovimiento() == MovimientoStock.TipoMovimiento.ROTACION) {
                actualizarStockOrigen(movimiento.getDepositoOrigen(), producto, -cantidad);
                actualizarStockDestino(movimiento.getDepositoDestino(), producto, cantidad);
            }
        }
    }

    private void actualizarStockOrigen(Deposito deposito, Producto producto, int cantidad) {
        Stock stock = stockRepository.findByDepositoAndProducto(deposito.getId(), producto.getId())
            .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado en el depósito origen"));

        if (stock.getCantidad() + cantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente para el producto " + producto.getDescripcion());
        }
        stock.setCantidad(stock.getCantidad() + cantidad);
        stockRepository.save(stock);
    }

    private void actualizarStockDestino(Deposito deposito, Producto producto, int cantidad) {
        // Verificar si existe un stock para este producto en el depósito
        Stock stock = stockRepository.findByDepositoAndProducto(deposito.getId(), producto.getId())
            .orElse(new Stock());

        // Asignar el producto y depósito al stock si es nuevo
        stock.setProducto(producto);
        stock.setDeposito(deposito);

        // Actualizar la cantidad del stock
        stock.setCantidad(stock.getCantidad() + cantidad);

        // Guardar el stock actualizado
        stockRepository.save(stock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovimiento(@PathVariable Long id) {
        if (movimientoStockRepository.existsById(id)) {
            movimientoStockRepository.deleteById(id);
            return ResponseEntity.ok("Movimiento de stock eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movimiento de stock no encontrado");
        }
    }
}