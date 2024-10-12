package com.depomanager.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "movimiento_stock")
public class MovimientoStock {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter @Setter
    @Column(name = "accion", length = 50, nullable = false)
    private String accion;

    @Getter @Setter
    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @Getter @Setter
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Getter @Setter
    @Column(name = "fecha")
    private Date fecha;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_deposito", nullable = false)
    private Deposito deposito;
    
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Getter @Setter
    @OneToMany(mappedBy = "movimientoStock")
    private List<DetalleMovimiento> listaDetalleMovimiento;

    @Getter @Setter
    @Column(name = "nro_comprobante", length = 50)
    private String nroComprobante;
}