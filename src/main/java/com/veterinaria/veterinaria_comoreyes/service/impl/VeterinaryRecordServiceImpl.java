package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.InfoVeterinaryRecordForTableDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.RecentMedicalRecordDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordStatsDTO;
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
import java.util.stream.Collectors;

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
    public Page<InfoVeterinaryRecordForTableDTO> getAllInfoVeterinaryRecordsByAnimal(Long animalId, Pageable pageable) {
        return repository.findAllInfoVeterinaryRecordsByAnimalId(animalId, pageable)
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

    @Override
    public List<RecentMedicalRecordDTO> getRecentRecordsByEmployee(Long employeeId) {
        List<Object[]> rows = repository.findRecentMedicalRecordsByEmployee(employeeId);

        return rows.stream().map(row -> new RecentMedicalRecordDTO(
                ((Number) row[0]).longValue(),       // veterinaryRecordId
                ((Number) row[1]).longValue(),       // careId
                (String) row[2],                    // animalName
                (String) row[3],                    // breedName
                (String) row[4],                    // clientFullName
                (String) row[5],                    // serviceName
                (String) row[6],                    // recordMedicalDate
                (String) row[7],                    // diagnosis
                (String) row[8],                    // treatment
                (String) row[9],                    // observations
                (String) row[10]                     // status
        )).collect(Collectors.toList());
    }

    @Override
    public VeterinaryRecordStatsDTO getVeterinaryRecordStatsByEmployee(Long employeeId) {
        List<Object[]> result = repository.getVeterinaryRecordStatsByEmployee(employeeId);
        if (result.isEmpty()) {
            return new VeterinaryRecordStatsDTO(0, 0, 0, 0);
        }

        Object[] row = result.get(0);
        return new VeterinaryRecordStatsDTO(
                ((Number) row[0]).intValue(), // total
                ((Number) row[1]).intValue(), // EN_CURSO
                ((Number) row[2]).intValue(), // COMPLETADO
                ((Number) row[3]).intValue()  // OBSERVACION
        );
    }
}
