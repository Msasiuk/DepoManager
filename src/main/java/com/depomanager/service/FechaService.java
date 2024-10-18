package com.depomanager.service;

import java.util.Date;
import org.springframework.stereotype.Service;

import com.depomanager.model.Fechas;

//Contiene metodos que se repiten en las entidades con fecha inicio y fecha fin
@Service
public class FechaService {

    // Retorna la fecha actual
    public Date obtenerFechaActual() {
        return new Date();
    }

    // Retorna la fecha máxima (31/12/9999)
    public Date obtenerFechaMaxima() {
        return new Date(Long.MAX_VALUE);
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