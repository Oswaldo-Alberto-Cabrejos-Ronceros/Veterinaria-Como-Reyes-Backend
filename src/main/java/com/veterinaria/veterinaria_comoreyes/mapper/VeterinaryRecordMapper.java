package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Care;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VeterinaryRecordMapper {

    @Mapping(source = "careId", target = "care", qualifiedByName = "mapCare")
    @Mapping(source = "employeeId", target = "employee", qualifiedByName = "mapEmployee")
    @Mapping(target = "statusVeterinaryRecord", constant = "EN_CURSO")
    VeterinaryRecord toEntity(VeterinaryRecordDTO dto);

    @Mapping(source = "care.careId", target = "careId")
    @Mapping(source = "employee.employeeId", target = "employeeId")
    VeterinaryRecordDTO toDTO(VeterinaryRecord entity);

    @Named("mapCare")
    static Care mapCare(Long id) {
        Care care = new Care();
        care.setCareId(id);
        return care;
    }

    @Named("mapEmployee")
    static Employee mapEmployee(Long id) {
        Employee emp = new Employee();
        emp.setEmployeeId(id);
        return emp;
    }
}
