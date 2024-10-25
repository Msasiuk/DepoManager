package com.depomanager.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Esta clase representa la relación entre un depósito y un producto, incluyendo la cantidad de ese producto almacenada y la fecha de vencimiento (si aplica).
@NoArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {

	@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_producto")
    @Getter @Setter
    private Producto producto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_deposito", nullable = false)
    @Getter @Setter
    private Deposito deposito;

    @Getter @Setter
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Getter @Setter
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    public Stock(Producto producto, Deposito deposito, int cantidad) {
        this.producto = producto;
        this.deposito = deposito;
        this.cantidad = cantidad;
    }
}