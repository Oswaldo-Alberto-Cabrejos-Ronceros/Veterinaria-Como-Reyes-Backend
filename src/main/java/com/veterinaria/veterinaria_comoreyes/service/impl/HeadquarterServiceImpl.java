package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeadquarterServiceImpl implements IHeadquarterService {


    private HeadquarterRepository headquarterRepository;
    private FilterStatus filterStatus;

    @Autowired
    public HeadquarterServiceImpl(HeadquarterRepository headquarterRepository, FilterStatus filterStatus){
        this.headquarterRepository=headquarterRepository;
        this.filterStatus=filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public HeadquarterDTO getHeadquarterById(Long id) {
        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        return HeadquarterMapper.maptoHeadquarterDTO(hq);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HeadquarterDTO> getAllHeadquarters() {
        filterStatus.activeFilterStatus(true);
        return headquarterRepository.findAll()
                .stream()
                .map(HeadquarterMapper::maptoHeadquarterDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public HeadquarterDTO createHeadquarter(HeadquarterDTO dto) {
        filterStatus.activeFilterStatus(true);
        Headquarter entity = HeadquarterMapper.maptoHeadquarter(dto);
        Headquarter saved = headquarterRepository.save(entity);
        return HeadquarterMapper.maptoHeadquarterDTO(saved);
    }

    @Transactional
    @Override
    public HeadquarterDTO updateHeadquarter(Long id, HeadquarterDTO dto) {
        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));

        hq.setPhone(dto.getPhone());
        hq.setAddress(dto.getAddress());
        hq.setEmail(dto.getEmail());
        hq.setDistrict(dto.getDistrict());
        hq.setProvince(dto.getProvince());
        hq.setDepartment(dto.getDepartment());

        Headquarter updated = headquarterRepository.save(hq);
        return HeadquarterMapper.maptoHeadquarterDTO(updated);
    }

    @Transactional
    @Override
    public void deleteHeadquarter(Long id) {
        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        hq.setStatus(false); // Desactivado
        headquarterRepository.save(hq);
    }
}
