package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.mapper.AppointmentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final HeadquarterVetServiceRepository headquarterVetServiceRepository;
    private final AnimalRepository animalRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentMapper appointmentMapper,
                                  HeadquarterVetServiceRepository headquarterVetServiceRepository,
                                  AnimalRepository animalRepository,
                                  EmployeeRepository employeeRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.headquarterVetServiceRepository = headquarterVetServiceRepository;
        this.animalRepository = animalRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {
        HeadquarterVetService hvs = headquarterVetServiceRepository
                .findByIdAndStatusIsTrue(dto.getHeadquarterVetServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio veterinario en sede no encontrado"));

        Animal animal = animalRepository.findByAnimalIdAndStatusIsTrue(dto.getAnimalId())
                .orElseThrow(() -> new RuntimeException("Animal no encontrado"));

        Employee employee = null;
        if (dto.getAssignedEmployeeId() != null) {
            employee = employeeRepository.findByEmployeeIdAndStatusTrue(dto.getAssignedEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Empleado asignado no encontrado"));
        }

        Appointment appointment = appointmentMapper.toEntity(dto);
        appointment.setHeadquarterVetService(hvs);
        appointment.setAnimal(animal);
        appointment.setEmployee(employee);
        appointment.setCreationDate(java.time.LocalDateTime.now());
        appointment.setStatusAppointment(StatusAppointment.PROGRAMADA); // ✅ Estado inicial

        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return appointmentMapper.toResponseDTO(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO dto) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        appointmentMapper.updateEntityFromDto(dto, appointment);

        if (dto.getHeadquarterVetServiceId() != null) {
            HeadquarterVetService hvs = headquarterVetServiceRepository
                    .findByIdAndStatusIsTrue(dto.getHeadquarterVetServiceId())
                    .orElseThrow(() -> new RuntimeException("Servicio veterinario en sede no encontrado"));
            appointment.setHeadquarterVetService(hvs);
        }

        if (dto.getAnimalId() != null) {
            Animal animal = animalRepository.findByAnimalIdAndStatusIsTrue(dto.getAnimalId())
                    .orElseThrow(() -> new RuntimeException("Animal no encontrado"));
            appointment.setAnimal(animal);
        }

        if (dto.getAssignedEmployeeId() != null) {
            Employee employee = employeeRepository.findByEmployeeIdAndStatusTrue(dto.getAssignedEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Empleado asignado no encontrado"));
            appointment.setEmployee(employee);
        }

        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

    @Transactional
    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatusAppointment(StatusAppointment.CANCELADA);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public AppointmentResponseDTO confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatusAppointment(StatusAppointment.CONFIRMADA);
        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponseDTO completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        appointment.setStatusAppointment(StatusAppointment.COMPLETADA);
        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

}
