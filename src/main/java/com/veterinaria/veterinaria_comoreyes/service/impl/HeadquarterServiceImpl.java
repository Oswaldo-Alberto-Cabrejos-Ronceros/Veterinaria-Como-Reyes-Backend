package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Employee.EmployeeInfoPublicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterEmployeesDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.exception.HeadquarterNotValidException;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.repository.EmployeeRepository;
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

    @Autowired
    private HeadquarterRepository headquarterRepository;
    @Autowired
    private HeadquarterMapper headquarterMapper;
    @Autowired
    private FilterStatus filterStatus;

    private EmployeeRepository employeeRepository;

    @Autowired
    public HeadquarterServiceImpl(HeadquarterRepository headquarterRepository,EmployeeRepository employeeRepository, HeadquarterMapper headquarterMapper, FilterStatus filterStatus){
        this.headquarterRepository=headquarterRepository;
        this.headquarterMapper=headquarterMapper;
        this.filterStatus=filterStatus;
        this.employeeRepository=employeeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public HeadquarterDTO getHeadquarterById(Long id) {
        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        return headquarterMapper.mapToHeadquarterDTO(hq);
    }

    @Transactional(readOnly = true)
    @Override
    public List<HeadquarterDTO> getAllHeadquarters() {
        filterStatus.activeFilterStatus(true);
        return headquarterRepository.findAll()
                .stream()
                .map(headquarterMapper::mapToHeadquarterDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public HeadquarterDTO createHeadquarter(HeadquarterDTO dto) {
        Headquarter entity = headquarterMapper.mapToHeadquarter(dto);
        entity.setStatus(true);
        Headquarter saved = headquarterRepository.save(entity);
        return headquarterMapper.mapToHeadquarterDTO(saved);
    }

    @Transactional
    @Override
    public HeadquarterDTO updateHeadquarter(Long id, HeadquarterDTO dto) {
        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));

        hq.setPhone(dto.getPhone());
        hq.setAddress(dto.getAddress());
        hq.setEmail(dto.getEmail());
        hq.setDistrict(dto.getDistrict());
        hq.setProvince(dto.getProvince());
        hq.setDepartment(dto.getDepartment());
        hq.setStartTime(dto.getStartTime());
        hq.setEndTime(dto.getEndTime());

        Headquarter updated = headquarterRepository.save(hq);
        return headquarterMapper.mapToHeadquarterDTO(updated);
    }

    @Transactional
    @Override
    public void deleteHeadquarter(Long id) {

        filterStatus.activeFilterStatus(true);
        Headquarter hq = headquarterRepository.findByHeadquarterIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("Headquarter not found with id: " + id));
        hq.setStatus(false); // Desactivado
        headquarterRepository.save(hq);

    }

    // Validar si la sede existe y esta disponible
    @Override
    public void validateHeadquarterAvailable(Long id) {
        boolean exist= headquarterRepository.existsByHeadquarterIdAndStatusIsTrue(id);
        if(!exist){
            throw new HeadquarterNotValidException("Sede no disponoble");
        }
    }

    @Override
    public List<HeadquarterEmployeesDTO> getAllActiveHeadquartersWithActiveEmployees() {
        // Buscar todas las sedes activas
        List<Headquarter> headquarters = headquarterRepository.findAllByStatusTrue();

        // Mapear cada sede con sus empleados activos
        return headquarters.stream().map(hq -> {
            // Buscar empleados activos para cada sede
            List<Employee> activeEmployees = employeeRepository.findByHeadquarter_HeadquarterIdAndStatusTrue(hq.getHeadquarterId());

            // Mapear empleados a DTO
            List<EmployeeInfoPublicDTO> employeeInfoList = activeEmployees.stream()
                    .map(emp -> new EmployeeInfoPublicDTO(emp.getName() + " " + emp.getLastName(), emp.getDirImage()))
                    .toList();

            return new HeadquarterEmployeesDTO(hq.getName(), employeeInfoList);
        }).toList();
    }

    @Transactional
    @Override
    public void activateHeadquarter(Long headquarterId) {
        headquarterRepository.activateHeadquarter(headquarterId);
    }
}
