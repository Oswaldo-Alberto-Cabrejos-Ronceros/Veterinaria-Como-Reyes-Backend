package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class,componentModel = "spring")
public interface SpecieMapper {

        SpecieDTO mapToSpecieDTO(Specie specie);

        Specie mapToSpecie(SpecieDTO specieDTO);

        List<SpecieDTO> mapToSpecieDTOList(List<Specie> species);
}