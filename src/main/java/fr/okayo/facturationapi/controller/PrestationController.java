package fr.okayo.facturationapi.controller;

import fr.okayo.facturationapi.model.Prestation;
import fr.okayo.facturationapi.repository.PrestationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
public class PrestationController { // Controller pour gérer les prestations

    private final PrestationRepository prestationRepository;

    public PrestationController(PrestationRepository prestationRepository) {
        this.prestationRepository = prestationRepository;
    }

    @GetMapping
    public List<Prestation> getAll() { // Récupère toutes les prestations
        return prestationRepository.findAll();
    }

    @PostMapping
    public Prestation create(@RequestBody Prestation prestation) {
        return prestationRepository.save(prestation);
    }
}
