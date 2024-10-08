package com.depomanager.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "proveedor")
public class Proveedor {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "cuit_cuil", length = 50, nullable = false)
    private String cuitCuil;

    @Getter @Setter
    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Getter @Setter
    @Column(name = "razon_social", length = 255)
    private String razonSocial;

    @Getter @Setter
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Getter @Setter
    @Column(name = "fecha_fin")
    private Date fechaFin;
}