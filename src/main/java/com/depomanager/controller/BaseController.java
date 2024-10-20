package com.depomanager.controller;

import java.util.HashMap;
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
    private JpaRepository<T, ID> repository;

    // Crear entidad (sin validación de código)
    @PostMapping
    public ResponseEntity<Map<String, String>> create(@RequestBody T entity) {
        Map<String, String> response = new HashMap<>();

        // Guardar la entidad sin validar el código
        repository.save(entity);
        response.put("message", "Entidad creada exitosamente.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    // Actualizar entidad (sin validación de código)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable ID id, @RequestBody T entity) {
        Map<String, String> response = new HashMap<>();

        // Verificar si existe la entidad por ID antes de actualizar
        if (!repository.existsById(id)) {
            response.put("message", "Entidad no encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Guardar la entidad sin validar el código
        repository.save(entity);
        response.put("message", "Entidad modificada exitosamente.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Eliminar un elemento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable ID id) {
        Map<String, String> response = new HashMap<>();

        if (repository.existsById(id)) {
            repository.deleteById(id);
            response.put("message", "Elemento eliminado exitosamente.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response.put("message", "Elemento no encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}