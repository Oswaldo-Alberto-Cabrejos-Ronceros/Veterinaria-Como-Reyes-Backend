package com.veterinaria.veterinaria_comoreyes.service;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.InfoVeterinaryRecordForTableDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVeterinaryRecordService {
    VeterinaryRecordDTO create(VeterinaryRecordDTO dto);

    VeterinaryRecordDTO update(Long id, VeterinaryRecordDTO dto);

    List<VeterinaryRecordDTO> findAll();

    VeterinaryRecordDTO findById(Long id);

    VeterinaryRecordDTO updateStatus(Long id, StatusVeterinaryRecord status);


    Page<InfoVeterinaryRecordForTableDTO> getAllInfoVeterinaryRecordsByAnimal(Long animalId, Pageable pageable);
}
