package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.dto.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryServiceDTO;
import com.veterinaria.veterinaria_comoreyes.entity.VeterinaryService;
import com.veterinaria.veterinaria_comoreyes.mapper.CategoryMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.SpecieMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.VeterinaryServiceMapper;
import com.veterinaria.veterinaria_comoreyes.repository.VeterinaryServiceRepository;
import com.veterinaria.veterinaria_comoreyes.service.ICategoryService;
import com.veterinaria.veterinaria_comoreyes.service.ISpecieService;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryServiceService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeterinaryServiceServiceImpl implements IVeterinaryServiceService {

    private final VeterinaryServiceRepository veterinaryServiceRepository;
    private final ISpecieService specieService;
    private final ICategoryService categoryService;
    private final FilterStatus filterStatus;
    private final VeterinaryServiceMapper veterinaryServiceMapper;
    private final SpecieMapper specieMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public VeterinaryServiceServiceImpl(
            VeterinaryServiceRepository veterinaryServiceRepository,
            ISpecieService specieService,
            ICategoryService categoryService,
            FilterStatus filterStatus,
            VeterinaryServiceMapper veterinaryServiceMapper,
            SpecieMapper specieMapper,
            CategoryMapper categoryMapper
    ) {
        this.veterinaryServiceRepository = veterinaryServiceRepository;
        this.specieService = specieService;
        this.categoryService = categoryService;
        this.filterStatus = filterStatus;
        this.veterinaryServiceMapper = veterinaryServiceMapper;
        this.specieMapper = specieMapper;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public VeterinaryServiceDTO getServiceById(Long id) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceRepository
                .findByServiceIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Veterinary Service Not Found with id:" + id));
        return veterinaryServiceMapper.mapToServiceDTO(service);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VeterinaryServiceDTO> getAllServices() {
        filterStatus.activeFilterStatus(true);
        return veterinaryServiceRepository.findAll().stream()
                .map(veterinaryServiceMapper::mapToServiceDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId) {
        filterStatus.activeFilterStatus(true);
        SpecieDTO specieDTO = specieService.getSpecieById(specieId);
        return veterinaryServiceRepository.findAllBySpecie(specieMapper.mapToSpecie(specieDTO)).stream()
                .map(veterinaryServiceMapper::mapToServiceDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId) {
        filterStatus.activeFilterStatus(true);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        return veterinaryServiceRepository.findAllByCategory(categoryMapper.maptoCategory(categoryDTO)).stream()
                .map(veterinaryServiceMapper::mapToServiceDTO)
                .toList();
    }

    @Transactional
    @Override
    public VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceMapper.mapToService(veterinaryServiceDTO);
        VeterinaryService saved = veterinaryServiceRepository.save(service);
        return veterinaryServiceMapper.mapToServiceDTO(saved);
    }

    @Transactional
    @Override
    public VeterinaryServiceDTO updateService(Long id, VeterinaryServiceDTO dto) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceRepository.findByServiceIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Veterinary Service Not Found with id:" + id));

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setCategory(dto.getCategory());
        service.setSpecie(dto.getSpecie());
        service.setDirImage(dto.getDirImage());
        service.setDuration(dto.getDuration());

        VeterinaryService updated = veterinaryServiceRepository.save(service);
        return veterinaryServiceMapper.mapToServiceDTO(updated);
    }

    @Transactional
    @Override
    public void deleteService(Long id) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceRepository.findByServiceIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Veterinary Service Not Found with id:" + id));
        service.setStatus(false);
        veterinaryServiceRepository.save(service);
    }
}
