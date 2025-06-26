package com.example.pcshop.controller;

import com.example.pcshop.model.Pc;
import com.example.pcshop.repository.PcRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pc")
public class PcController {

    private final PcRepository repository;

    public PcController(PcRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pc> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Pc save(@RequestBody Pc pc) {
        return repository.save(pc);
    }
}
