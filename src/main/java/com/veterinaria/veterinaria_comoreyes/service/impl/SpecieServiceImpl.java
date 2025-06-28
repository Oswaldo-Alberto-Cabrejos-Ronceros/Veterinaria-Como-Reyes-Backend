package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Specie;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.repository.SpecieRepository;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<SpecieListDTO> searchSpecies(String name, String imagePath, Boolean status, Pageable pageable) {
        String redisKey = String.format(
                "species:page=%d:size=%d:name=%s:imagePath=%s:status=%s",
                pageable.getPageNumber(), pageable.getPageSize(),
                name != null ? name : "null",
                imagePath != null ? imagePath : "null",
                status != null ? status : "null");

        @SuppressWarnings("unchecked")
        List<SpecieListDTO> cachedList = (List<SpecieListDTO>) redisTemplate.opsForValue().get(redisKey);

        Object rawTotal = redisTemplate.opsForValue().get(redisKey + ":total");
        Long totalCount = null;
        if (rawTotal instanceof Integer) {
            totalCount = ((Integer) rawTotal).longValue();
        } else if (rawTotal instanceof Long) {
            totalCount = (Long) rawTotal;
        }

        if (cachedList != null && totalCount != null) {
            System.out.println("[REDIS HIT] Clave: " + redisKey);
            return new PageImpl<>(cachedList, pageable, totalCount);
        }

        System.out.println("[REDIS MISS] Consultando DB para clave: " + redisKey);
        Page<SpecieListDTO> resultPage = specieRepository.searchSpecies(name, imagePath, status, pageable);

        redisTemplate.opsForValue().set(redisKey, resultPage.getContent());
        redisTemplate.opsForValue().set(redisKey + ":total", resultPage.getTotalElements());

        return resultPage;
    }

}
