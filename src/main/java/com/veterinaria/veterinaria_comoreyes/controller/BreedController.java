package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Breed.BreedDTO;
import com.veterinaria.veterinaria_comoreyes.service.IBreedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breed")
public class BreedController {

    private final IBreedService breedService;

    @Autowired
    public BreedController(IBreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public ResponseEntity<List<BreedDTO>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }

    @GetMapping("/specie/{id}")
    public ResponseEntity<List<BreedDTO>> getBreedsBySpecie(@PathVariable Long id) {
        return ResponseEntity.ok(breedService.getBreedsBySpecies(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BreedDTO> getSpecieById(@PathVariable Long id) {
        return ResponseEntity.ok(breedService.getBreedById(id));
    }

    @PostMapping
    public ResponseEntity<BreedDTO> createBreed(@RequestBody BreedDTO breedDTO) {
        return ResponseEntity.ok(breedService.createBreed(breedDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BreedDTO> updateBreed(@PathVariable Long id, @RequestBody BreedDTO breedDTO) {
        return ResponseEntity.ok(breedService.updateBreed(id, breedDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBreed(@PathVariable Long id) {
        breedService.deleteBreed(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{breedId}/activate")
    public ResponseEntity<Void> activateBreed(@PathVariable Long breedId) {
        breedService.activateBreed(breedId);
        return ResponseEntity.ok().build();
    }

}
