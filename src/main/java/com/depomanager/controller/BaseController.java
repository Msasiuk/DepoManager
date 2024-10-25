package com.depomanager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<T, ID> {

    @Autowired
	protected JpaRepository<T, ID> repository;
    
    // Obtener todos los elementos
    @GetMapping
    public List<T> getAll() {
        return repository.findAll();
    }
    
    // Obtener un elemento por ID
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    // Crear una nueva entidad (Plantilla)
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody T entity) {
        if (existeEntidadDuplicada(entity)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "El elemento ya existe."));
        }
        preGuardarEntidad(entity);  // Gancho para personalización
        repository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Elemento creado exitosamente."));
    }

    // Actualizar entidad
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable ID id, @RequestBody T entity) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Elemento no encontrado."));
        }
        preGuardarEntidad(entity);  // Gancho para modificaciones
        repository.save(entity);
        return ResponseEntity.ok(Map.of("message", "Elemento actualizado."));
    }

    // Eliminar un elemento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Elemento eliminado."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Elemento no encontrado."));
    }

    // Métodos abstractos para personalización
    protected abstract boolean existeEntidadDuplicada(T entity);

    protected void preGuardarEntidad(T entity) {}
}