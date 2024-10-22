package com.depomanager.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "deposito")
public class Deposito extends Fechas {

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
 
    @OneToMany(mappedBy="deposito",cascade=CascadeType.ALL)
    @Getter @Setter
    private Set<DepositoProducto> depositoproducto;
    
}