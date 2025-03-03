package com.depomanager.movimiento.models;

import java.time.LocalDate;
import java.util.List;

import com.depomanager.deposito.models.Deposito;
import com.depomanager.producto.models.DetalleProducto;
import com.depomanager.proveedor.models.Proveedor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Gestiona las entradas, salidas y rotaciones de productos entre depósitos.
@NoArgsConstructor
@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock {

	@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Getter @Setter
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDate fechaMovimiento;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "deposito_origen_id")
    private Deposito depositoOrigen;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "deposito_destino_id")
    private Deposito depositoDestino;
    
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Getter @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "movimiento_stock_id")
    private List<DetalleProducto> detalles = List.of();

    public enum TipoMovimiento {
        INGRESO, EGRESO, ROTACION
    }

}