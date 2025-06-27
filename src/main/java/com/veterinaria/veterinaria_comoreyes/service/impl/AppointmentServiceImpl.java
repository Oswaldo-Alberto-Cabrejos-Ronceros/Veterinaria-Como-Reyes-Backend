package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.*;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import com.veterinaria.veterinaria_comoreyes.mapper.AppointmentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final HeadquarterVetServiceRepository headquarterVetServiceRepository;
    private final IHeadquarterVetServiceService headquarterVetServiceService;
    private final IPaymentService paymentService;
    private final IAnimalService animalService;
    private final AnimalRepository animalRepository;
    private final EmployeeRepository employeeRepository;
    private final IPaymentMethodService paymentMethodService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
            AppointmentMapper appointmentMapper,
            HeadquarterVetServiceRepository headquarterVetServiceRepository,
            IHeadquarterVetServiceService headquarterVetServiceService, IPaymentService paymentService,
            IAnimalService animalService,
            AnimalRepository animalRepository,
            EmployeeRepository employeeRepository, IPaymentMethodService paymentMethodService,
            PaymentRepository paymentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.headquarterVetServiceRepository = headquarterVetServiceRepository;
        this.headquarterVetServiceService = headquarterVetServiceService;
        this.paymentService = paymentService;
        this.animalService = animalService;
        this.animalRepository = animalRepository;
        this.employeeRepository = employeeRepository;
        this.paymentMethodService = paymentMethodService;
        this.paymentRepository = paymentRepository;
    }

    public void validateSpeciesOfAnimalWithService(Long animalId, Long headquarterServiceId) {
        String nameSpecieOfService = headquarterVetServiceService.nameSpecie(headquarterServiceId);
        String nameSpecieOfAnimal = animalService.findSpecieNameByAnimalId(animalId);
        if (nameSpecieOfAnimal.equalsIgnoreCase(nameSpecieOfService)) {
            throw new RuntimeException(
                    "Este animal no puede ser seleccionado con este servicio porque su especie no coincide.");
        }
    }

    public void validateAvailability(LocalDateTime scheduleDateTime, Long headquarterServiceId) {
        boolean isAvailable = appointmentRepository.isAppointmentSlotAvailable(scheduleDateTime, headquarterServiceId);

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

        // Persistir
        Appointment savedAppointment = appointmentRepository.save(appointment);

        Double priceService = headquarterVetServiceService.priceService(dto.getHeadquarterVetServiceId());
        // Crear el OrderPaymentDTO
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
    public UserBuyerDTO getInfoForPaymentMerPago(Long idAppoinment) {
        Payment payment = paymentRepository.findByAppointment_AppointmentIdAndStatus(idAppoinment, "PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No hay pago pendiente para esta cita"));

        // Armar el DTO a partir de las relaciones:
        String title = payment.getAppointment()
                .getHeadquarterVetService()
                .getVeterinaryService()
                .getName();

        Long idOrderPayment = payment.getPaymentId();
        Integer quantity = 1; // Asumimos 1
        Double unitPrice = payment.getAppointment()
                .getHeadquarterVetService()
                .getVeterinaryService()
                .getPrice();

        // Construir y devolver el DTO
        UserBuyerDTO userBuyerDTO = new UserBuyerDTO();
        userBuyerDTO.setTitle(title);
        userBuyerDTO.setIdOrderPayment(idOrderPayment);
        userBuyerDTO.setQuantity(quantity);
        userBuyerDTO.setUnitPrice(unitPrice);

        return userBuyerDTO;
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

    @Override
    public List<TimesForTurnDTO> getAvailableTimesForTurn(Long headquarterVetServiceId, String date) {
        List<FormatTimeDTO> allTimes = appointmentRepository.timesAvailableForServiceAndDate(headquarterVetServiceId,
                date);

        List<FormatTimeDTO> manana = new ArrayList<>();
        List<FormatTimeDTO> tarde = new ArrayList<>();
        List<FormatTimeDTO> noche = new ArrayList<>();

        for (FormatTimeDTO dto : allTimes) {
            String time = dto.getTime(); // Ejemplo: "09:20"
            int hour = Integer.parseInt(time.split(":")[0]);

            if (hour >= 9 && hour < 12) {
                manana.add(dto);
            } else if (hour >= 12 && hour < 17) {
                tarde.add(dto);
            } else if (hour >= 17) {
                noche.add(dto);
            }
        }

        List<TimesForTurnDTO> result = new ArrayList<>();
        if (!manana.isEmpty())
            result.add(new TimesForTurnDTO("MAÑANA", manana));
        if (!tarde.isEmpty())
            result.add(new TimesForTurnDTO("TARDE", tarde));
        if (!noche.isEmpty())
            result.add(new TimesForTurnDTO("NOCHE", noche));

        return result;
    }

    // @Override
    // public List<BasicServiceForAppointmentDTO> getServicesByHeadquarterAndSpeciesForAppointment(Long headquarterId,
    //         Long speciesId) {
    //     return appointmentRepository.findServicesByHeadquarterAndSpeciesForAppointment(headquarterId, speciesId);
    // }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentListDTO> searchAppointments(LocalDate scheduleDateTime, StatusAppointment statusAppointment,
            Long headquarterVetServiceId, Long employeeId, Long animalId, Pageable pageable) {

        String redisKey = String.format(
                "appointments:page=%d:size=%d:date=%s:status=%s:hvsId=%s:empId=%s:animalId=%s",
                pageable.getPageNumber(), pageable.getPageSize(),
                scheduleDateTime != null ? scheduleDateTime : "null",
                statusAppointment != null ? statusAppointment : "null",
                headquarterVetServiceId != null ? headquarterVetServiceId : "null",
                employeeId != null ? employeeId : "null",
                animalId != null ? animalId : "null");

        @SuppressWarnings("unchecked")
        List<AppointmentListDTO> cached = (List<AppointmentListDTO>) redisTemplate.opsForValue().get(redisKey);
        Long total = (Long) redisTemplate.opsForValue().get(redisKey + ":total");

        if (cached != null && total != null) {
            System.out.println("[REDIS HIT] " + redisKey);
            return new PageImpl<>(cached, pageable, total);
        }

        System.out.println("[REDIS MISS] " + redisKey);
        Page<AppointmentListDTO> pageResult = appointmentRepository.searchAppointments(
                scheduleDateTime, statusAppointment, headquarterVetServiceId, employeeId, animalId, pageable);

        redisTemplate.opsForValue().set(redisKey, pageResult.getContent());
        redisTemplate.opsForValue().set(redisKey + ":total", pageResult.getTotalElements());

        return pageResult;
    }
    public List<BasicServiceForAppointmentDTO> getServicesByHeadquarterAndSpeciesForAppointment(Long headquarterId, Long speciesId) {
        List<Object[]> rows = appointmentRepository.findServiceDetailsForAppointment(headquarterId, speciesId);

        return rows.stream().map(row -> new BasicServiceForAppointmentDTO(
                ((Number) row[0]).longValue(),                                // headquarterServiceId
                ((Number) row[1]).longValue(),                                // serviceId
                row[2].toString(),                                            // name
                row[3].toString(),                                            // description
                row[4] != null
                        ? new BigDecimal(row[4].toString()).setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO,                                        // price con 2 decimales
                ((Number) row[5]).intValue(),                                 // duration
                row[6].toString(),                                            // specieName
                row[7] != null ? row[7].toString() : null,                    // serviceImageUrl
                row[8].toString()                                             // categoryName
        )).toList();
    }
}
