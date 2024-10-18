package com.depomanager.model;

import com.depomanager.controller.HasCodigo;

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
@Table(name = "producto")
public class Producto extends Fechas implements HasCodigo {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @Getter @Setter
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

    @Getter @Setter
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Getter @Setter
    @Column(name = "stock_maximo")
    private int stockMaximo;

    @Getter @Setter
    @Column(name = "stock_minimo")
    private int stockMinimo;

	public Producto(String codigo, String descripcion, TipoProducto tipoProducto, int cantidad) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tipoProducto = tipoProducto;
		this.cantidad = cantidad;
	}
    
}