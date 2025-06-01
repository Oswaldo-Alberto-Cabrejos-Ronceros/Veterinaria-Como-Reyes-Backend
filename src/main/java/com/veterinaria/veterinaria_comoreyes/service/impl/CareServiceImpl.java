package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Care;
import com.veterinaria.veterinaria_comoreyes.entity.StatusCare;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.mapper.CareMapper;
import com.veterinaria.veterinaria_comoreyes.repository.CareRepository;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareServiceImpl implements ICareService {

    private final CareRepository careRepository;
    private final CareMapper careMapper;

    @Autowired
    public CareServiceImpl(CareRepository careRepository, CareMapper careMapper) {
        this.careRepository = careRepository;
        this.careMapper = careMapper;
    }

    @Override
    public CareDTO getCareById(Long id) {
        Care care = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attencion con id: " + id + " no encontrada"));
        return careMapper.toDTO(care);
    }

    @Override
    public List<CareDTO> getCareByAppointment(Long appointmentId) {
        return careRepository.findByAppointment_AppointmentId(appointmentId)
                .stream()
                .map(careMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CareDTO> getAllCares() {
        return careRepository.findAll()
                .stream()
                .map(careMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CareDTO createCare(CareDTO careDTO) {
        Care care = careMapper.toEntity(careDTO);
        care.setStatusCare(StatusCare.EN_CURSO);
        Care saved = careRepository.save(care);
        return careMapper.toDTO(saved);
    }

    @Override
    public CareDTO completeCare(Long id) {
        Care care = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));
        care.setStatusCare(StatusCare.COMPLETADO);
        Care updated = careRepository.save(care);
        return careMapper.toDTO(updated);
    }

    @Override
    public CareDTO updateCare(Long id, CareDTO careDTO) {
        Care existingCare = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));

        careMapper.updateEntityFromDto(careDTO, existingCare);
        Care updated = careRepository.save(existingCare);
        return careMapper.toDTO(updated);
    }

    // @Override
    // public void deleteCare(Long id) {
    //     Care care = careRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));
    //     careRepository.delete(care);
    // }
}
