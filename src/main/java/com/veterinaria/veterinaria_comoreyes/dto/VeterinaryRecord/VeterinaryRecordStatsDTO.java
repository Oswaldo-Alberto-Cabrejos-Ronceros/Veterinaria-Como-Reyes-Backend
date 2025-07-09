package com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeterinaryRecordStatsDTO {
    private Integer total;
    private Integer enCurso;
    private Integer completado;
    private Integer observacion;
}
