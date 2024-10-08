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
@Table(name = "usuario")
public class Usuario {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "alias", length = 50, nullable = false)
    private String alias;

    @Getter @Setter
    @Column(name = "contrasenia", length = 255, nullable = false)
    private String contrasenia;

    @Getter @Setter
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Getter @Setter
    @Column(name = "fecha_fin")
    private Date fechaFin;
}