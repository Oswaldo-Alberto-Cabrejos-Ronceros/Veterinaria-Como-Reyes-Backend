package com.veterinaria.veterinaria_comoreyes.mapper;

import com.veterinaria.veterinaria_comoreyes.config.GlobalMapperConfig;
import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class,componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodDTO mapToPaymentMethodDTO(PaymentMethod paymentMethod);

    PaymentMethod mapToPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    List<PaymentMethodDTO> mapToPaymentMethodDTOList(List<PaymentMethod> paymentMethods);
}