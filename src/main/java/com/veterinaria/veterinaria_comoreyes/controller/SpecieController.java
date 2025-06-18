package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
public class SpecieController {

    @Autowired
    private ISpecieService specieService;

    @GetMapping
    public List<SpecieDTO> getAllSpecies() {
        return specieService.getAllSpecies();
    }

    @GetMapping("/{id}")
    public SpecieDTO getSpecieById(@PathVariable Long id) {
        return specieService.getSpecieById(id);
    }

    @PostMapping
    public SpecieDTO createSpecie(@Valid @RequestBody SpecieDTO specieDTO) {
        return specieService.createSpecie(specieDTO);
    }

    @PutMapping("/{id}")
    public SpecieDTO updateSpecie(@PathVariable Long id, @Valid @RequestBody SpecieDTO specieDTO) {
        return specieService.updateSpecie(id, specieDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSpecie(@PathVariable Long id) {
        specieService.deleteSpecie(id);
    }

    @GetMapping("/paginated")
    public Page<SpecieDTO> getPaginatedSpecies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return specieService.getAllSpeciesPaginated(page, size);
    }
}
