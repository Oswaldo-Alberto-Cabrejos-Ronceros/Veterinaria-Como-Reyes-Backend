package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalListDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAnimalService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    private final IAnimalService animalService;

    public AnimalController(IAnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<AnimalDTO>> getAllAnimals() {
        List<AnimalDTO> animals = animalService.getAllAnimals();
        return ResponseEntity.ok().body(animals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> getAnimalById(@PathVariable Long id) {
        return ResponseEntity.ok().body(animalService.getAnimalById(id));
    }

    /*
     * @GetMapping("/client/{id}")
     * public ResponseEntity<List<AnimalDTO>> getAnimalByClientId(@PathVariable Long
     * id) {
     * return ResponseEntity.ok().body(animalService.getAnimalsByClient(id));
     * }
     */

    @PostMapping
    public ResponseEntity<AnimalDTO> createAnimal(@RequestBody AnimalDTO animalDTO) {
        return ResponseEntity.ok().body(animalService.createAnimal(animalDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> updateAnimal(@PathVariable Long id, @RequestBody AnimalDTO animalDTO) {
        return ResponseEntity.ok().body(animalService.updateAnimal(id, animalDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }

    // esto es para el panel de cliente la info basica de los animales
    @GetMapping("/client/{id}")
    public ResponseEntity<List<AnimalInfoForClientDTO>> getAnimalsByClientId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(animalService.getAnimalsByClientId(id));
    }

    @PutMapping("/{animalId}/activate")
    public ResponseEntity<Void> activateAnimal(@PathVariable Long animalId) {
        animalService.activateAnimal(animalId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public Page<AnimalListDTO> searchFullAnimals(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String specie,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean status,
            @PageableDefault(size = 10) Pageable pageable) {
        return animalService.searchAnimals(name, owner, specie, breed, gender, status, pageable);
    }
}
