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

import java.util.List;
import java.util.logging.Filter;

@Service
public class VeterinaryServiceServiceImpl implements IVeterinaryServiceService {

    private final VeterinaryServiceRepository veterinaryServiceRepository;
    private final ISpecieService specieService;
    private final ICategoryService categoryService;
    private final FilterStatus filterStatus;

    @Autowired
    public VeterinaryServiceServiceImpl(VeterinaryServiceRepository veterinaryServiceRepository, ISpecieService specieService, ICategoryService categoryService, FilterStatus filterStatus) {
        this.veterinaryServiceRepository = veterinaryServiceRepository;
        this.specieService = specieService;
        this.categoryService = categoryService;
        this.filterStatus=filterStatus;
    }

    @Override
    public VeterinaryServiceDTO getServiceById(Long id) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
        return VeterinaryServiceMapper.mapToServiceDTO(service);
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServices() {
        filterStatus.activeFilterStatus(true);
        return veterinaryServiceRepository.findAll().stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId) {
        filterStatus.activeFilterStatus(true);
        SpecieDTO specieDTO = specieService.getSpecieById(specieId);
        return veterinaryServiceRepository.findAllBySpecie(SpecieMapper.maptoSpecie(specieDTO)).stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId) {
        filterStatus.activeFilterStatus(true);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        return veterinaryServiceRepository.findAllByCategory(CategoryMapper.maptoCategory(categoryDTO)).stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService service = veterinaryServiceRepository.save(VeterinaryServiceMapper.mapToService(veterinaryServiceDTO));
        return VeterinaryServiceMapper.mapToServiceDTO(service);
    }

    @Override
    public VeterinaryServiceDTO updateService(Long id,VeterinaryServiceDTO veterinaryServiceDTO) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService veterinaryService = veterinaryServiceRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
        veterinaryService.setName(veterinaryServiceDTO.getName());
        veterinaryService.setDescription(veterinaryServiceDTO.getDescription());
        veterinaryService.setCategory(veterinaryServiceDTO.getCategory());
        veterinaryService.setSpecie(veterinaryServiceDTO.getSpecie());
        veterinaryService.setDirImage(veterinaryServiceDTO.getDirImage());
        veterinaryService.setDuration(veterinaryServiceDTO.getDuration());
       VeterinaryService saved= veterinaryServiceRepository.save(veterinaryService);
        return VeterinaryServiceMapper.mapToServiceDTO(saved);
    }

    @Override
    public void deleteService(Long id) {
        filterStatus.activeFilterStatus(true);
        VeterinaryService veterinaryService = veterinaryServiceRepository.findByIdAndStatusIsTrue(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
        veterinaryService.setStatus(false);//disabled
        veterinaryServiceRepository.save(veterinaryService);
    }
}
