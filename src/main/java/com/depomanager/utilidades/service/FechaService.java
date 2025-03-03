package com.depomanager.utilidades.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

import com.depomanager.utilidades.models.Fechas;

//Contiene metodos que se repiten en las entidades con fecha inicio y fecha fin
@Service
public class FechaService {

	 // Retorna la fecha actual
    public LocalDate obtenerFechaActual() {
        return LocalDate.now();
    }

    // Retorna la fecha máxima (31/12/9999)
    public LocalDate obtenerFechaMaxima() {
        return LocalDate.of(9999, 12, 31);
    }

    // Establece fechas por defecto si no se proporcionan
    public <T extends Fechas> void establecerFechasPorDefecto(T entidad) {
        if (entidad.getFechaInicio() == null) {
            entidad.setFechaInicio(obtenerFechaActual());
        }
        if (entidad.getFechaFin() == null) {
            entidad.setFechaFin(obtenerFechaMaxima());
        }
    }
}