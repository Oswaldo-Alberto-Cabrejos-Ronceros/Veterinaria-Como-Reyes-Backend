package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import org.mapstruct.*;

@Mapper(config = GlobalMapperConfig.class,
        componentModel = "spring")
public interface AppointmentMapper {

    // para el frontend
    AppointmentResponseDTO toResponseDTO(Appointment appointment);

    // Para el backend, de Request a Entity
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "assignedEmployeeId", target = "employee.employeeId")
    @Mapping(source = "animalId", target = "animal.animalId")
    Appointment toEntity(AppointmentRequestDTO dto);

    // Para actualizar una entidad existente desde un DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "headquarterVetServiceId", target = "headquarterVetService.id")
    @Mapping(source = "assignedEmployeeId", target = "employee.employeeId")
    @Mapping(source = "animalId", target = "animal.animalId")
    void updateEntityFromDto(AppointmentRequestDTO dto, @MappingTarget Appointment entity);


}
