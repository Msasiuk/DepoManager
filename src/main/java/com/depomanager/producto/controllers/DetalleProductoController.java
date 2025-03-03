package com.depomanager.producto.controllers;

import com.depomanager.producto.models.DetalleProducto;
import com.depomanager.producto.repository.IDetalleProductoRepository;
import com.depomanager.utilidades.controllers.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//Se mantiene la clase por la api, ver posible implementacion en futuro
@RestController
@RequestMapping("/api/detalle-producto")
public class DetalleProductoController extends BaseController<DetalleProducto, Long> {

    @Autowired
    private IDetalleProductoRepository detalleProductoRepository;
    
    @Override
    protected boolean existeEntidadDuplicada(DetalleProducto entity) {
        return detalleProductoRepository.existsById(entity.getId());
    }
}