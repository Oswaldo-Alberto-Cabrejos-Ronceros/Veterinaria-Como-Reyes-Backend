package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;

public class SpecieMapper {

        public static SpecieDTO maptoSpecieDTO(Specie specie) {
                return new SpecieDTO(
                                specie.getSpecieId(),
                                specie.getName(),
                                specie.getImagePath(),
                                specie.getStatus());
        }

        public static Specie maptoSpecie(SpecieDTO specieDTO) {
                return new Specie(
                                specieDTO.getSpecieId(),
                                specieDTO.getName(),
                                specieDTO.getImagePath(),
                                specieDTO.getStatus());
        }
}