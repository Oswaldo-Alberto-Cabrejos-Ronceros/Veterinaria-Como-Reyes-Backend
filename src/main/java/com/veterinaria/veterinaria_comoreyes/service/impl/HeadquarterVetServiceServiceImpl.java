package com.veterinaria.veterinaria_comoreyes.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veterinaria.veterinaria_comoreyes.dto.HeadquarterVetServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.HeadquarterVetService;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterVetServiceMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.VeterinaryServiceMapper;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterVetServiceRepository;
import com.veterinaria.veterinaria_comoreyes.repository.VeterinaryServiceRepository;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterVetServiceService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;

@Service
public class HeadquarterVetServiceServiceImpl implements IHeadquarterVetServiceService {

    public final HeadquarterVetServiceRepository headquarterVetServiceRepository;
    public final HeadquarterVetServiceMapper headquarterVetServiceMapper;
    public final HeadquarterRepository headquarterRepository;
    public final HeadquarterMapper headquarterMapper;
    public final VeterinaryServiceRepository veterinaryServiceRepository;
    public final VeterinaryServiceMapper veterinaryServiceMapper;
    public final FilterStatus filterStatus;

    @Autowired
    public HeadquarterVetServiceServiceImpl(HeadquarterVetServiceRepository headquarterVetServiceRepository,
            HeadquarterVetServiceMapper headquarterVetServiceMapper,
            HeadquarterRepository headquarterRepository,
            HeadquarterMapper headquarterMapper,
            VeterinaryServiceRepository veterinaryServiceRepository,
            VeterinaryServiceMapper veterinaryServiceMapper,
            FilterStatus filterStatus) {
        this.headquarterVetServiceRepository = headquarterVetServiceRepository;
        this.headquarterVetServiceMapper = headquarterVetServiceMapper;
        this.headquarterRepository = headquarterRepository;
        this.headquarterMapper = headquarterMapper;
        this.veterinaryServiceRepository = veterinaryServiceRepository;
        this.veterinaryServiceMapper = veterinaryServiceMapper;
        this.filterStatus = filterStatus;
    }

    @Override
    public List<HeadquarterVetServiceDTO> callAllHeadquarterVetServiceByHeadquarterId() {
        filterStatus.activeFilterStatus(true);
        return headquarterVetServiceRepository.findAll().stream()
                .map(headquarterVetServiceMapper::mapToHeadquarterVetServiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HeadquarterVetServiceDTO> callHeadquarterVetServiceByHeadquarter(Long headquarterId) {
        filterStatus.activeFilterStatus(true);
        Headquarter headquarter = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(headquarterId)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + headquarterId));
        return headquarterVetServiceRepository.findByHeadquarter(headquarter).stream()
                .map(headquarterVetServiceMapper::mapToHeadquarterVetServiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HeadquarterVetServiceDTO> getHeadquarterVetServiceById(Long id) {
        filterStatus.activeFilterStatus(true);
        return headquarterVetServiceRepository.findByIdAndStatusIsTrue(id)
                .map(headquarterVetServiceMapper::mapToHeadquarterVetServiceDTO);
    }

    @Transactional
    @Override
    public HeadquarterVetServiceDTO createHeadquarterVetService(HeadquarterVetServiceDTO dto) {
        Headquarter headquarter = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(dto.getHeadquarterId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        VeterinaryService service = veterinaryServiceRepository
                .findByServiceIdAndStatusIsTrue(dto.getService().getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio veterinario no encontrado"));

        boolean exists = headquarterVetServiceRepository.existsByHeadquarterAndVetService(headquarter, service);
        if (exists) {
            throw new RuntimeException("La relación ya existe entre esta sede y servicio.");
        }

        HeadquarterVetService entity = new HeadquarterVetService(null, headquarter, service);
        return headquarterVetServiceMapper.mapToHeadquarterVetServiceDTO(headquarterVetServiceRepository.save(entity));
    }

    @Transactional
    @Override
    public HeadquarterVetServiceDTO updateHeadquarterVetService(Long id, HeadquarterVetServiceDTO dto) {
        HeadquarterVetService existing = headquarterVetServiceRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Relación sede-servicio no encontrada"));

        Headquarter headquarter = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(dto.getHeadquarterId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        VeterinaryService service = veterinaryServiceRepository
                .findByServiceIdAndStatusIsTrue(dto.getService().getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio veterinario no encontrado"));

        existing.setHeadquarter(headquarter);
        existing.setVeterinaryService(service);

        return headquarterVetServiceMapper
                .mapToHeadquarterVetServiceDTO(headquarterVetServiceRepository.save(existing));
    }

    @Transactional
    @Override
    public void deleteHeadquarterVetService(Long id) {
        filterStatus.activeFilterStatus(true);
        HeadquarterVetService headquarterVetService = headquarterVetServiceRepository.findByIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada con ID: " + id));
        headquarterVetService.setStatus(false); // Cambia el estado a inactivo
        headquarterVetServiceRepository.save(headquarterVetService);
    }
}
