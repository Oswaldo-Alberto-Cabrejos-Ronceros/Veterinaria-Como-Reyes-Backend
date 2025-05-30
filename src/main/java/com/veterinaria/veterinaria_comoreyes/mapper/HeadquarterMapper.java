package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
=======
import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;

import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class,componentModel = "spring")
public interface HeadquarterMapper {

    HeadquarterDTO mapToHeadquarterDTO(Headquarter headquarter);

    Headquarter mapToHeadquarter(HeadquarterDTO dto);

    List<HeadquarterDTO> mapToHeadquarterDTOList(List<Headquarter> headquarters);

    List<Headquarter> mapToHeadquarterList(List<HeadquarterDTO> dtos);
}
