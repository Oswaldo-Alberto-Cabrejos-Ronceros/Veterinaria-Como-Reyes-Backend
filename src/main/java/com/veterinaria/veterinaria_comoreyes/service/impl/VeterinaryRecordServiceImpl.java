package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.InfoVeterinaryRecordForTableDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.entity.StatusVeterinaryRecord;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryRecord;
import com.veterinaria.veterinaria_comoreyes.mapper.VeterinaryRecordMapper;
import com.veterinaria.veterinaria_comoreyes.repository.VeterinaryRecordRepository;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VeterinaryRecordServiceImpl implements IVeterinaryRecordService {
    private final VeterinaryRecordRepository repository;
    private final VeterinaryRecordMapper mapper;

    @Override
    public VeterinaryRecordDTO create(VeterinaryRecordDTO dto) {
        VeterinaryRecord entity = mapper.toEntity(dto);
        entity.setDateCreated(LocalDate.now());
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public VeterinaryRecordDTO update(Long id, VeterinaryRecordDTO dto) {
        VeterinaryRecord existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro veterinario no encontrado"));

        existing.setDiagnosis(dto.getDiagnosis());
        existing.setTreatment(dto.getTreatment());
        existing.setObservations(dto.getObservations());
        existing.setResultUrl(dto.getResultUrl());

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public List<VeterinaryRecordDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public VeterinaryRecordDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Registro veterinario no encontrado"));
    }

    @Override
    public VeterinaryRecordDTO updateStatus(Long id, StatusVeterinaryRecord status) {
        VeterinaryRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro veterinario no encontrado"));

        record.setStatusVeterinaryRecord(status);
        return mapper.toDTO(repository.save(record));
    }

    @Override
    public Page<InfoVeterinaryRecordForTableDTO> getAllInfoVeterinaryRecords(Pageable pageable) {
        return repository.findAllInfoVeterinaryRecords(pageable)
                .map(this::mapToDto);
    }

    private InfoVeterinaryRecordForTableDTO mapToDto(Object[] row) {
        InfoVeterinaryRecordForTableDTO dto = new InfoVeterinaryRecordForTableDTO();
        dto.setId(((Number) row[0]).longValue());
        dto.setDate(row[1] != null ? row[1].toString() : null);
        dto.setNameHeadquarter((String) row[2]);
        dto.setNameEmployee((String) row[3]);
        dto.setDiagnosis((String) row[4]);
        dto.setTreatment((String) row[5]);
        dto.setObservation((String) row[6]);
        dto.setResultUrl((String) row[7]);
        dto.setStatus(StatusVeterinaryRecord.valueOf((String) row[8]));
        return dto;
    }
}
