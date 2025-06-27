package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;

import java.util.List;

public interface IVeterinaryRecordService {
    VeterinaryRecordDTO create(VeterinaryRecordDTO dto);

    VeterinaryRecordDTO update(Long id, VeterinaryRecordDTO dto);

    List<VeterinaryRecordDTO> findAll();

    VeterinaryRecordDTO findById(Long id);

    VeterinaryRecordDTO updateStatus(Long id, StatusVeterinaryRecord status);
}
