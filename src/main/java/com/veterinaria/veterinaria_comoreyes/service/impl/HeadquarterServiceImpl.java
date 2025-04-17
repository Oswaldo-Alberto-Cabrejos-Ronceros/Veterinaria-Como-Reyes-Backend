package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeadquarterServiceImpl implements IHeadquarterService {

    @Autowired
    private HeadquarterRepository headquarterRepository;

    @Override
    public HeadquarterDTO getHeadquarterById(Long id) {
        Headquarter hq = headquarterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        return HeadquarterMapper.maptoHeadquarterDTO(hq);
    }

    @Override
    public List<HeadquarterDTO> getAllHeadquarters() {
        return headquarterRepository.findAll()
                .stream()
                .map(HeadquarterMapper::maptoHeadquarterDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HeadquarterDTO createHeadquarter(HeadquarterDTO dto) {
        Headquarter entity = HeadquarterMapper.maptoHeadquarter(dto);
        Headquarter saved = headquarterRepository.save(entity);
        return HeadquarterMapper.maptoHeadquarterDTO(saved);
    }

    @Override
    public HeadquarterDTO updateHeadquarter(Long id, HeadquarterDTO dto) {
        Headquarter hq = headquarterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));

        hq.setPhone(dto.getPhone());
        hq.setAddress(dto.getAddress());
        hq.setEmail(dto.getEmail());
        hq.setDistrict(dto.getDistrict());
        hq.setProvince(dto.getProvince());
        hq.setDepartment(dto.getDepartment());
        hq.setStatus(dto.getStatus());

        Headquarter updated = headquarterRepository.save(hq);
        return HeadquarterMapper.maptoHeadquarterDTO(updated);
    }

    @Override
    public void deleteHeadquarter(Long id) {
        Headquarter hq = headquarterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        hq.setStatus(0); // Desactivado
        headquarterRepository.save(hq);
    }
}
