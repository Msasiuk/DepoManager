package com.depomanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<T extends HasCodigo, ID>  {

    @Autowired
    private JpaRepository<T, ID> repository;

    //Método para crear entidades
    @PostMapping
    public ResponseEntity<String> create(@RequestBody T entity) {
        // Validar que el codigo o cuilCuit no sea nulo o vacío
        if (entity.getCodigo() == null || entity.getCodigo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El código no puede estar vacío.");
        }

        // Validar si el código ya existe
        if (repository.findAll().stream().anyMatch(e -> e.getCodigo().equals(entity.getCodigo()))) {
            return ResponseEntity.badRequest().body("El código ya existe.");
        }

        repository.save(entity);
        return ResponseEntity.ok("Entidad creada exitosamente.");
    }
    
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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Actualizar un elemento por ID
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable ID id, @RequestBody T entity) {
        // Validar si el código está en uso por otra entidad
        if (repository.findAll().stream()
                .anyMatch(e -> e.getCodigo().equals(entity.getCodigo()) && !e.getId().equals(id))) {
            return ResponseEntity.badRequest().body("El código ya está en uso por otro registro.");
        }

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.save(entity);
        return ResponseEntity.ok("Entidad modificada exitosamente.");
    }

    // Eliminar un elemento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok("Elemento eliminado exitosamente.");
        }
        return ResponseEntity.notFound().build();
    }
}