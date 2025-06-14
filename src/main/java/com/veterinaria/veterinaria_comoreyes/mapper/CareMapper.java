package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Care;
import org.mapstruct.*;

@Mapper(config = GlobalMapperConfig.class, componentModel = "spring")
public interface CareMapper {

    // Para mostrar en el frontend
    @Mapping(source = "headquarterVetService.id", target = "headquarterVetServiceId")
    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    @Mapping(source = "animal.animalId", target = "animalId")
    CareDTO toDTO(Care care);

    // Para guardar desde el backend
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "animalId", target = "animal.animalId")
    Care toEntity(CareDTO dto);

    // Para actualizar sin sobrescribir campos nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "animalId", target = "animal.animalId")
    void updateEntityFromDto(CareDTO dto, @MappingTarget Care entity);
}
