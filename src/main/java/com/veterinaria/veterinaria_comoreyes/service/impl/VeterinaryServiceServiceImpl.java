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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinaryServiceServiceImpl implements IVeterinaryServiceService {

    private final VeterinaryServiceRepository veterinaryServiceRepository;
    private final ISpecieService specieService;
    private final ICategoryService categoryService;

    public VeterinaryServiceServiceImpl(VeterinaryServiceRepository veterinaryServiceRepository,ISpecieService specieService,ICategoryService categoryService) {
        this.veterinaryServiceRepository = veterinaryServiceRepository;
        this.specieService = specieService;
        this.categoryService = categoryService;
    }

    @Override
    public VeterinaryServiceDTO getServiceById(Long id) {
        VeterinaryService service = veterinaryServiceRepository.findById(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
        return VeterinaryServiceMapper.mapToServiceDTO(service);
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServices() {
        return veterinaryServiceRepository.findAll().stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServicesBySpecie(Long specieId) {
        SpecieDTO specieDTO = specieService.getSpecieById(specieId);
        return veterinaryServiceRepository.findAllBySpecie(SpecieMapper.maptoSpecie(specieDTO)).stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public List<VeterinaryServiceDTO> getAllServicesByCategory(Long categoryId) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        return veterinaryServiceRepository.findAllByCategory(CategoryMapper.maptoCategory(categoryDTO)).stream().map(VeterinaryServiceMapper::mapToServiceDTO).toList();
    }

    @Override
    public VeterinaryServiceDTO createService(VeterinaryServiceDTO veterinaryServiceDTO) {
        VeterinaryService service = veterinaryServiceRepository.save(VeterinaryServiceMapper.mapToService(veterinaryServiceDTO));
        return VeterinaryServiceMapper.mapToServiceDTO(service);
    }

    @Override
    public VeterinaryServiceDTO updateService(Long id,VeterinaryServiceDTO veterinaryServiceDTO) {
        VeterinaryService veterinaryService = veterinaryServiceRepository.findById(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
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
        VeterinaryService veterinaryService = veterinaryServiceRepository.findById(id).orElseThrow(()->new RuntimeException("Veterinary Service Not Found with id:" + id));
        veterinaryService.setStatus((byte)0);//disabled
        veterinaryServiceRepository.save(veterinaryService);
    }
}
