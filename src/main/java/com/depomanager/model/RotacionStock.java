package com.depomanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "rotacion_stock")
public class RotacionStock extends IMovimientoStock {
    // No hay campos adicionales
}