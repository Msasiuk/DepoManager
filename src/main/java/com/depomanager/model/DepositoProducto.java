package com.depomanager.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "depositoproducto")
public class DepositoProducto {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="deposito_id",nullable=false)
	private Deposito deposito;
	
	@ManyToOne
	@JoinColumn(name="producto_id",nullable=false)
	private Producto producto;
	
	@Getter @Setter
	// Validar si Boolean tieneVencimiento es false entonces 9999-12-31 y si es true que puedan ingresar la fecha de vencimiento.
	@Column(name = "fecha_vencimiento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date fechaVencimiento;
	
	
	 @Getter @Setter
	    @Column(name = "cantidad")
	    private int cantidad;

	
	
	
	
} 
