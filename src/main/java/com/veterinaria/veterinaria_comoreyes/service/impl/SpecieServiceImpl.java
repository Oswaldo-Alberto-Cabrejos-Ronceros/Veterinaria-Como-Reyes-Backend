package com.veterinaria.veterinaria_comoreyes.service.impl;


import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.TopSpeciesByAppointmentsDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecieServiceImpl implements ISpecieService {

    private final SpecieRepository specieRepository;
    private final SpecieMapper specieMapper;
    private final FilterStatus filterStatus;

    @Autowired
    public SpecieServiceImpl(SpecieRepository specieRepository, SpecieMapper specieMapper, FilterStatus filterStatus) {
        this.specieRepository = specieRepository;
        this.specieMapper = specieMapper;
        this.filterStatus = filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public SpecieDTO getSpecieById(Long id) {
        filterStatus.activeFilterStatus(true);
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        return specieMapper.mapToSpecieDTO(specie);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SpecieDTO> getAllSpecies() {
        filterStatus.activeFilterStatus(true);
        return specieRepository.findAll().stream()
                .map(specieMapper::mapToSpecieDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SpecieDTO createSpecie(SpecieDTO specieDTO) {
        Specie specie = specieMapper.mapToSpecie(specieDTO);
        specie.setStatus(true);
        Specie savedSpecie = specieRepository.save(specie);
        return specieMapper.mapToSpecieDTO(savedSpecie);
    }

    @Transactional
    @Override
    public SpecieDTO updateSpecie(Long id, SpecieDTO specieDTO) {
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));

        specie.setName(specieDTO.getName());
        specie.setImagePath(specieDTO.getImagePath());

        Specie updated = specieRepository.save(specie);
        return specieMapper.mapToSpecieDTO(updated);
    }

    @Transactional
    @Override
    public void deleteSpecie(Long id) {
        Specie specie = specieRepository.findBySpecieIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Specie not found with id: " + id));
        specie.setStatus(false);
        specieRepository.disableBreedsBySpecieId(id); // Desactiva las razas asociadas
        specieRepository.save(specie);
    }

    @Transactional
    @Override
    public void activateSpecie(Long specieId) {
        specieRepository.activateSpecie(specieId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecieListDTO> searchSpecies(String name, Boolean status, Pageable pageable) {
        filterStatus.activeFilterStatus(true); // Aplica filtro lógico global si corresponde
        return specieRepository.searchSpeciesWithFilters(name, status, pageable);
    }

    @Override
    public TopSpeciesByAppointmentsDTO getTopSpeciesGeneral() {
        List<Object[]> rows = specieRepository.findTopSpeciesWithMostCompletedAppointments();
        return buildTopSpeciesDTO(rows);
    }

    @Override
    public TopSpeciesByAppointmentsDTO getTopSpeciesByHeadquarter(Long headquarterId) {
        List<Object[]> rows = specieRepository.findTopSpeciesWithMostCompletedAppointmentsByHeadquarter(headquarterId);
        return buildTopSpeciesDTO(rows);
    }

    private TopSpeciesByAppointmentsDTO buildTopSpeciesDTO(List<Object[]> rows) {
        List<String> speciesNames = new ArrayList<>();
        List<Long> appointmentCounts = new ArrayList<>();

        for (Object[] row : rows) {
            speciesNames.add((String) row[0]);
            appointmentCounts.add(((Number) row[1]).longValue());
        }

        return new TopSpeciesByAppointmentsDTO(speciesNames, appointmentCounts);
    }
}

