package com.veterinaria.veterinaria_comoreyes.dto.Care;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareStatsTodayDTO {
    private Long totalCares;        // Total de cares atentidas
    private Long todayCares;        // Total de cares atendidas hoy
}
