package com.depomanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "detalle_movimiento")
public class DetalleMovimiento {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_movimiento", nullable = false)
    private MovimientoStock movimientoStock;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}