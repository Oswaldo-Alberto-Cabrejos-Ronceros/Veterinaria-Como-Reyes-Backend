package com.veterinaria.veterinaria_comoreyes.dto.Appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimesForTurnDTO {
    String turn;
    List<FormatTimeDTO> times;
}
