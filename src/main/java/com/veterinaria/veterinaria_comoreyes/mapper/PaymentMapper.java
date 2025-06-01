package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Payment;
import org.mapstruct.*;

@Mapper(config = GlobalMapperConfig.class, componentModel = "spring")
public interface PaymentMapper {

    // Entity -> DTO
    @Mapping(source = "appointment.appointmentId", target = "appointmentId")
    @Mapping(source = "care.careId", target = "careId")
    @Mapping(source = "paymentMethod.paymentMethodId", target = "paymentMethodId")
    PaymentDTO toDTO(Payment payment);

    // DTO -> Entity
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "careId", target = "care.careId")
    @Mapping(source = "paymentMethodId", target = "paymentMethod.paymentMethodId")
    Payment toEntity(PaymentDTO dto);

    // DTO -> Entity (update parcial, ignora nulls)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "appointmentId", target = "appointment.appointmentId")
    @Mapping(source = "careId", target = "care.careId")
    @Mapping(source = "paymentMethodId", target = "paymentMethod.paymentMethodId")
    void updateEntityFromDto(PaymentDTO dto, @MappingTarget Payment entity);
}
