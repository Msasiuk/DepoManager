package com.depomanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "egreso_stock")
public class EgresoStock extends IMovimientoStock {
    // No hay campos adicionales
}