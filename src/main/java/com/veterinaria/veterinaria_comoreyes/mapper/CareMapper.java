package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Care;
import org.mapstruct.*;

@Mapper(config = GlobalMapperConfig.class, componentModel = "spring")
public interface CareMapper {

    @Mapping(source = "headquarterVetService.id", target = "headquarterVetServiceId")
    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    @Mapping(source = "animal.animalId", target = "animalId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    CareDTO toDTO(Care care);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "animalId", target = "animal.animalId")
    @Mapping(source = "employeeId", target = "employee.employeeId")
    Care toEntity(CareDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "animalId", target = "animal.animalId")
    @Mapping(source = "employeeId", target = "employee.employeeId")
    void updateEntityFromDto(CareDTO dto, @MappingTarget Care entity);
}

