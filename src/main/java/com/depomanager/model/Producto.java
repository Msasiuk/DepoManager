package com.depomanager.model;

import java.util.Date;

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
public class Producto {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @Getter @Setter
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Getter @Setter
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Getter @Setter
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

    @Getter @Setter
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Getter @Setter
    @Column(name = "stock_maximo", nullable = false)
    private int stockMaximo;

    @Getter @Setter
    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo;
}