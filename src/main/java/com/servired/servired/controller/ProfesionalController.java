package com.servired.servired.controller;

import com.servired.servired.model.Profesional;
import com.servired.servired.repository.ProfesionalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesionales")
public class ProfesionalController {

    private final ProfesionalRepository repository;

    public ProfesionalController(ProfesionalRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Profesional crear(@RequestBody Profesional profesional) {
        return repository.save(profesional);
    }

    @GetMapping
    public List<Profesional> listar() {
        return repository.findAll();
    }
    @GetMapping("/{id}")
    public Profesional buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}