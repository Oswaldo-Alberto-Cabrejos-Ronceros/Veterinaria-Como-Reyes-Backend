package com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfoVeterinaryRecordForTableDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String  date;
    private String nameHeadquarter;
    private String nameEmployee;
    private String diagnosis;
    private String treatment;
    private String observation;
    private String resultUrl;
    private StatusVeterinaryRecord status;
}
