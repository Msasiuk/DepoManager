package com.depomanager.model;

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
public class Proveedor extends Fechas {
	
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "cuit_cuil", length = 50, nullable = false, unique = true)
    private String cuitCuil; //Cuit-cuil
    
    @Getter @Setter
    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @Getter @Setter
    @Column(name = "razon_social", length = 255)
    private String razonSocial;

}