package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class,componentModel = "spring")
public interface VeterinaryServiceMapper {

    VeterinaryServiceDTO mapToServiceDTO(VeterinaryService veterinaryService);

    VeterinaryService mapToService(VeterinaryServiceDTO veterinaryServiceDTO);
}
