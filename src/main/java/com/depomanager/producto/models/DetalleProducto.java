package com.depomanager.producto.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//Define los productos involucrados en cada movimiento.
@Entity
@Table(name = "detalle_producto")
public class DetalleProducto {

	@Getter
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_producto", nullable = false)
	@Getter @Setter
	private Producto producto;

	@Getter @Setter
	@Column(name = "cantidad", nullable = false)
	private int cantidad;

	@Getter @Setter
	@Column(name = "fecha_vencimiento")
	private LocalDate fechaVencimiento;
}