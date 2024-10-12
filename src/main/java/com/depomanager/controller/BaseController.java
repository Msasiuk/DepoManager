package com.depomanager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<T, ID> {

    @Autowired
    private JpaRepository<T, ID> repository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody T entity) {
        T savedEntity = repository.save(entity);
        return ResponseEntity.ok(savedEntity);
    }
    
    // Obtener todos los elementos
    @GetMapping
    public List<T> getAll() {
        return repository.findAll();
    }

    // Obtener un elemento por ID
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        Optional<T> entity = repository.findById(id);
        return entity.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un elemento por ID
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.save(entity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un elemento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Elemento eliminado exitosamente.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}