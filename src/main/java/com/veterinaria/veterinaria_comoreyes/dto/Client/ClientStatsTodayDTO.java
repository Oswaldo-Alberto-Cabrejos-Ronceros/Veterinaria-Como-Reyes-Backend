package com.veterinaria.veterinaria_comoreyes.dto.Client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatsTodayDTO {
    private Integer totalClientsAttended;
    private Integer todayClientsAttended;
}
