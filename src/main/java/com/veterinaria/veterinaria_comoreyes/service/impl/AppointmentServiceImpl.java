package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentResponseDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.mapper.AppointmentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final HeadquarterVetServiceRepository headquarterVetServiceRepository;
    private final IHeadquarterVetServiceService headquarterVetServiceService;
    private final  IPaymentService paymentService;
    private final IAnimalService animalService;
    private final AnimalRepository animalRepository;
    private final EmployeeRepository employeeRepository;
    private final IPaymentMethodService paymentMethodService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  AppointmentMapper appointmentMapper,
                                  HeadquarterVetServiceRepository headquarterVetServiceRepository, IHeadquarterVetServiceService headquarterVetServiceService, IPaymentService paymentService, IAnimalService animalService,
                                  AnimalRepository animalRepository,
                                  EmployeeRepository employeeRepository, IPaymentMethodService paymentMethodService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.headquarterVetServiceRepository = headquarterVetServiceRepository;
        this.headquarterVetServiceService = headquarterVetServiceService;
        this.paymentService = paymentService;
        this.animalService = animalService;
        this.animalRepository = animalRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodService = paymentMethodService;
    }

    public void validateSpeciesOfAnimalWithService(Long animalId, Long headquarterServiceId) {
        String nameSpecieOfAnimal =headquarterVetServiceService.nameService(headquarterServiceId);
        String nameSpecieOfService =animalService.findSpecieNameByAnimalId(animalId);
        if (!nameSpecieOfAnimal.equalsIgnoreCase(nameSpecieOfService)) {
            throw new RuntimeException("Este animal no puede ser seleccionado con este servicio porque su especie no coincide.");
        }
    }

    public void validateAvailability(LocalDateTime scheduleDateTime, Long headquarterServiceId) {
        boolean isAvailable = appointmentRepository.isAppointmentSlotAvailable(scheduleDateTime,headquarterServiceId);

        if (!isAvailable) {
            throw new RuntimeException("La fecha y hora seleccionadas estan ocupadas.");
        }
    }

    @Transactional
    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto) {

        // Validar el servicio veterinario en sede
        headquarterVetServiceService.validateHeadquarterVetService(dto.getHeadquarterVetServiceId());

        // Validar el cliente con el id del animal
        animalService.validateClientExistAndStatusForAnimalId(dto.getAnimalId());

        // Validar el animal id
        animalService.validateAnimalExistAndStatus(dto.getAnimalId());

        // Validar la especie del cliente con la especie del servicio
        validateSpeciesOfAnimalWithService(dto.getAnimalId(), dto.getHeadquarterVetServiceId());

        // Validar la disponibilidad
        validateAvailability(dto.getScheduleDateTime(), dto.getHeadquarterVetServiceId());

        // Validar el método de pago
        paymentMethodService.validePaymentMethod(dto.getPaymentMethodId());

        // Obtener las entidades necesarias
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

        // Crear la entidad
        Appointment appointment = appointmentMapper.toEntity(dto);
        appointment.setHeadquarterVetService(hvs);
        appointment.setAnimal(animal);
        appointment.setEmployee(employee);
        appointment.setCreationDate(LocalDateTime.now());
        appointment.setStatusAppointment(StatusAppointment.PROGRAMADA);

        // Persistir y retornar
        Appointment savedAppointment = appointmentRepository.save(appointment);
        Double priceService = headquarterVetServiceService.priceService(dto.getHeadquarterVetServiceId());
        // Crear el PaymentDTO
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAppointmentId(savedAppointment.getAppointmentId());
        paymentDTO.setPaymentMethodId(dto.getPaymentMethodId());
        paymentDTO.setStatus(PaymentStatus.PENDIENTE);
        paymentDTO.setAmount(priceService);
        paymentDTO.setPaymentDateTime(null);
        paymentDTO.setCareId(null);

        // Guardar el pago usando el método del servicio
        paymentService.createPayment(paymentDTO);

        return appointmentMapper.toResponseDTO(savedAppointment);

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
