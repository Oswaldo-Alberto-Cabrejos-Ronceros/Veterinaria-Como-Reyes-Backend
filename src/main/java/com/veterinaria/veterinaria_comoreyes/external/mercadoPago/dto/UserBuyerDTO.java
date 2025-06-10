package com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBuyerDTO {

    //nombre del servicio
    private String title;

    //id del payment
    private Long idOrderPayment;

    //cantidad del servicio
    private Integer quantity;

    //precio del servicio x unidad
    private Double unitPrice;


}
