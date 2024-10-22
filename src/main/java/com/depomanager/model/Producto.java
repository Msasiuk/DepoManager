package com.depomanager.model;


import java.util.Set;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto extends Fechas {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    
    //Creacion de tabla sobre la relacion con Deposito.
    @OneToMany(mappedBy="producto",cascade=CascadeType.ALL)
    @Getter @Setter
    private Set<DepositoProducto> depositoproducto;
 
    // No puede ser nulo y además debe ser unico, no pueden haber 2 productos con el mismo codigo.
    @Getter @Setter
    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    @Getter @Setter
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;
    
	@Getter @Setter
    @Column(name = "tiene_Vencimiento")
    private Boolean tieneVencimiento;
    

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

   
    @Getter @Setter
    @Column(name = "stock_maximo")
    private int stockMaximo;

    @Getter @Setter
    @Column(name = "stock_minimo")
    private int stockMinimo;
    
    @Getter @Setter
    @Column(name = "punto_reposicion")
    private int puntoReposicion;  
        
    
}