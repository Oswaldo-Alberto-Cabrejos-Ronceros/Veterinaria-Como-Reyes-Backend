package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VetServiceListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Service.VeterinaryServiceDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public VeterinaryServiceServiceImpl(
            VeterinaryServiceRepository veterinaryServiceRepository,
            ISpecieService specieService,
            ICategoryService categoryService,
            FilterStatus filterStatus,
            VeterinaryServiceMapper veterinaryServiceMapper,
            SpecieMapper specieMapper,
            CategoryMapper categoryMapper) {
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
        VeterinaryService service = veterinaryServiceMapper.mapToService(veterinaryServiceDTO);
        service.setStatus(true);
        VeterinaryService saved = veterinaryServiceRepository.save(service);
        return veterinaryServiceMapper.mapToServiceDTO(saved);
    }

    @Transactional
    @Override
    public VeterinaryServiceDTO updateService(Long id, VeterinaryServiceDTO dto) {
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
        VeterinaryService service = veterinaryServiceRepository.findByServiceIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Veterinary Service Not Found with id:" + id));
        service.setStatus(false);
        veterinaryServiceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VetServiceListDTO> searchVetServices(String name, Double price, String specieId, String categoryId,
            Boolean status, Pageable pageable) {
        String redisKey = String.format("vetServices:page=%d:size=%d:name=%s:price=%s:specie=%s:category=%s:status=%s",
                pageable.getPageNumber(), pageable.getPageSize(),
                name != null ? name : "null",
                price != null ? price : "null",
                specieId != null ? specieId : "null",
                categoryId != null ? categoryId : "null",
                status != null ? status : "null");

        @SuppressWarnings("unchecked")
        List<VetServiceListDTO> cached = (List<VetServiceListDTO>) redisTemplate.opsForValue().get(redisKey);
        Long total = (Long) redisTemplate.opsForValue().get(redisKey + ":total");

        if (cached != null && total != null) {
            System.out.println("[REDIS HIT] " + redisKey);
            return new PageImpl<>(cached, pageable, total);
        }

        System.out.println("[REDIS MISS] " + redisKey);
        Page<VetServiceListDTO> result = veterinaryServiceRepository.searchVetServices(name, price, specieId,
                categoryId, status, pageable);

        redisTemplate.opsForValue().set(redisKey, result.getContent());
        redisTemplate.opsForValue().set(redisKey + ":total", result.getTotalElements());

        return result;
    }

}
