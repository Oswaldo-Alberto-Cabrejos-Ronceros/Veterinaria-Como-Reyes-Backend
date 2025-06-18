package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecieServiceImpl implements ISpecieService {

    private final SpecieRepository specieRepository;
    private final SpecieMapper specieMapper;
    private final FilterStatus filterStatus;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        specieRepository.save(specie);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecieDTO> getAllSpeciesPaginated(int page, int size) {
        filterStatus.activeFilterStatus(true);
        String redisKey = "species:page=" + page + ":size=" + size;

        @SuppressWarnings("unchecked")
        List<SpecieDTO> cached = (List<SpecieDTO>) redisTemplate.opsForValue().get(redisKey);

        if (cached != null) {
            return new PageImpl<>(cached, PageRequest.of(page, size), cached.size());
        }

        Page<Specie> speciePage = specieRepository.findAll(PageRequest.of(page, size));
        List<SpecieDTO> dtoList = speciePage.getContent()
                .stream()
                .map(specieMapper::mapToSpecieDTO)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(redisKey, dtoList);
        return new PageImpl<>(dtoList, speciePage.getPageable(), speciePage.getTotalElements());
    }

}
