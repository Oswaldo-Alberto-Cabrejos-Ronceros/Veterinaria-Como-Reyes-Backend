package com.veterinaria.veterinaria_comoreyes.dto.Animal;

import java.time.LocalDate;

import com.veterinaria.veterinaria_comoreyes.entity.Breed;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimalListDTO {
    private Long animalId;
    private String name;
    private String gender;
    private String breedId;
    private String clientId;
    private String status;
}
