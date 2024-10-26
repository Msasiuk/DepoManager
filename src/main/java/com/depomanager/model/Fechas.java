package com.depomanager.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Fechas {

	@Getter @Setter
    @Column(name = "fecha_inicio")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Usar LocalDate de Java 8 es más adecuado para trabajar con fechas sin horas
    private LocalDate fechaInicio;

    @Getter @Setter
    @Column(name = "fecha_fin")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;
}