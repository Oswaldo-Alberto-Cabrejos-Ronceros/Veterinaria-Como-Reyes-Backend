package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Category;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeterinaryServiceRepository extends JpaRepository<VeterinaryService,Long> {
List<VeterinaryService> findAllBySpecie(Specie specie);

List<VeterinaryService> findAllByCategory(Category category);

}
