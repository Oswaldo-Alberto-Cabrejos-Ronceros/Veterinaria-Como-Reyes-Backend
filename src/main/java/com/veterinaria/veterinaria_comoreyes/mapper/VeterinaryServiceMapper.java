package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;

public class VeterinaryServiceMapper {
    public static VeterinaryServiceDTO mapToServiceDTO(VeterinaryService veterinaryService) {
        return new VeterinaryServiceDTO(
                veterinaryService.getServiceId(),
                veterinaryService.getName(),
                veterinaryService.getDescription(),
                veterinaryService.getPrice(),
                veterinaryService.getDuration(),
                veterinaryService.getDirImage(),
                veterinaryService.getSpecie(),
                veterinaryService.getCategory());
    }

    public static VeterinaryService mapToService(VeterinaryServiceDTO veterinaryServiceDTO) {
        return new VeterinaryService(
                veterinaryServiceDTO.getServiceId(),
                veterinaryServiceDTO.getName(),
                veterinaryServiceDTO.getDescription(),
                veterinaryServiceDTO.getPrice(),
                veterinaryServiceDTO.getDuration(),
                veterinaryServiceDTO.getDirImage(),
                veterinaryServiceDTO.getSpecie(),
                veterinaryServiceDTO.getCategory()
        );
    }
}
