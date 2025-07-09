package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.*;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareAndAppointmentPanelEmployeeDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentInfoForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import com.veterinaria.veterinaria_comoreyes.mapper.AppointmentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

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

            if (hour >= 0 && hour < 12) {
                manana.add(dto);
            } else if (hour >= 12 && hour < 17) {
                tarde.add(dto);
            } else {
                noche.add(dto);
            }
        }

        List<TimesForTurnDTO> result = new ArrayList<>();

        result.add(new TimesForTurnDTO("MAÑANA", manana));
        result.add(new TimesForTurnDTO("TARDE", tarde));
        result.add(new TimesForTurnDTO("NOCHE", noche));

        return result;
    }

    @Override
    public List<BasicServiceForAppointmentDTO> getServicesByHeadquarterAndSpeciesForAppointment(Long headquarterId,
            Long speciesId) {
        List<Object[]> rows = appointmentRepository.findServiceDetailsForAppointment(headquarterId, speciesId);

        return rows.stream().map(row -> new BasicServiceForAppointmentDTO(
                ((Number) row[0]).longValue(), // headquarterServiceId
                ((Number) row[1]).longValue(), // serviceId
                row[2].toString(), // name
                row[3].toString(), // description
                row[4] != null
                        ? new BigDecimal(row[4].toString()).setScale(2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO, // price con 2 decimales
                ((Number) row[5]).intValue(), // duration
                row[6].toString(), // specieName
                row[7] != null ? row[7].toString() : null, // serviceImageUrl
                row[8].toString() // categoryName
        )).toList();
    }

    @Override
    public List<InfoBasicAppointmentForPanelDTO> getAppointmentsForClientPanel(Long clientId) {
        List<Object[]> results = appointmentRepository.findInfoBasicAppointmentsByClientId(clientId);
        List<InfoBasicAppointmentForPanelDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            InfoBasicAppointmentForPanelDTO dto = new InfoBasicAppointmentForPanelDTO();
            dto.setId(row[0] != null ? ((Number) row[0]).longValue() : null); // id
            dto.setDate((String) row[1]); // appointment_date
            dto.setTime((String) row[2]); // appointment_time
            dto.setAnimalName((String) row[3]); // nombre del animal
            dto.setServiceName((String) row[4]); // nombre del servicio
            dto.setServiceDescription((String) row[5]); // descripción del servicio
            dto.setServiceImage((String) row[6]); // imagen del servicio
            dto.setCategoryServiceName((String) row[7]); // nombre de la categoría del servicio
            dto.setStatus((String) row[8]); // estado
            dto.setDuration(row[9] != null ? ((Number) row[9]).intValue() : null); // duración en minutos
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Page<AppointmentListDTO> searchAppointments(String day, String headquarter, String categoryService,
            String appointmentStatus, Pageable pageable) {

        StatusAppointment statusEnum = null;
        if (appointmentStatus != null) {
            try {
                statusEnum = StatusAppointment.valueOf(appointmentStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de cita no válido: " + appointmentStatus);
            }
        }

        return appointmentRepository.searchAppointments(
                day, headquarter, categoryService, statusEnum, pageable);
    }

    @Override
    public List<AppointmentInfoPanelAdminDTO> getAppointmentsByDateForPanelAdmin() {
        LocalDate today = LocalDate.now(); // fecha actual del sistema
        List<Object[]> rows = appointmentRepository.getAppointmentsInfoByDate(today);
        List<AppointmentInfoPanelAdminDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            AppointmentInfoPanelAdminDTO dto = new AppointmentInfoPanelAdminDTO(
                    ((Number) row[0]).longValue(), // appointment_id
                    (String) row[1], // animal_name
                    (String) row[2], // service_name
                    (String) row[3], // client_name
                    (String) row[4], // hour
                    (String) row[5] // status
            );
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<AppointmentInfoPanelAdminDTO> getAppointmentsInfoByDateAndHeadquarter(Long headquarterId) {
        LocalDate today = LocalDate.now();
        List<Object[]> rows = appointmentRepository.getAppointmentsInfoByDateAndHeadquarter(today, headquarterId);

        return rows.stream().map(row -> new AppointmentInfoPanelAdminDTO(
                ((Number) row[0]).longValue(), // appointment_id
                (String) row[1], // animal_name
                (String) row[2], // service_name
                (String) row[3], // client_name
                (String) row[4], // hour
                (String) row[5] // status
        )).toList();
    }

    @Override
    public AppointmentStatsTodayDTO getTodayAppointmentStats() {
        List<Object[]> resultList = appointmentRepository.getTodayAppointmentStats();
        Object[] row = resultList.get(0);

        Long total = row[0] != null ? ((Number) row[0]).longValue() : 0L;
        Long todayAppointments = row[1] != null ? ((Number) row[1]).longValue() : 0L;

        return new AppointmentStatsTodayDTO(total, todayAppointments);
    }

    @Override
    public AppointmentStatsTodayDTO getTodayAppointmentStatsByHeadquarter(Long headquarterId) {
        List<Object[]> resultList = appointmentRepository.getTodayAppointmentStatsByHeadquarter(headquarterId);
        Object[] row = resultList.get(0);

        Long total = row[0] != null ? ((Number) row[0]).longValue() : 0L;
        Long todayAppointments = row[1] != null ? ((Number) row[1]).longValue() : 0L;

        return new AppointmentStatsTodayDTO(total, todayAppointments);
    }

    @Override
    public List<CareAndAppointmentPanelEmployeeDTO> getAppointmentsForEmployee(Long employeeId) {
        List<Object[]> rows = appointmentRepository.findAppointmentsByEmployeeId(employeeId);

        System.out.println("Filas encontradas para empleado ID " + employeeId + ": " + rows.size());

        return rows.stream().map(row -> new CareAndAppointmentPanelEmployeeDTO(
                row[0] != null ? ((Number) row[0]).longValue() : null, // id
                row[1] != null ? row[1].toString() : null,             // type
                row[2] != null ? ((Number) row[0]).longValue() : null, // animalId
                row[3] != null ? row[2].toString() : null,             // animalName
                row[4] != null ? row[3].toString() : null,             // serviceName
                row[5] != null ? row[4].toString() : null,             // clientName
                row[6] != null ? row[5].toString() : null,             // date
                row[7] != null ? row[6].toString() : null,             // hour
                row[8] != null ? row[7].toString() : null              // status
        )).collect(Collectors.toList());
    }



    @Transactional
    @Override
    public AppointmentResponseDTO confirmAppointmentByEmail(Long id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        // Validar que la cita esté programada
        if (appointment.getStatusAppointment() != StatusAppointment.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden confirmar citas en estado PROGRAMADA mediante email");
        }
        // Cambiar el estado a CONFIRMADA
        appointment.setStatusAppointment(StatusAppointment.CONFIRMADA);
        Appointment confirmedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponseDTO(confirmedAppointment);
    }

    @Override
    public Optional<InfoAppointmentForPanelDTO> getAppointmentInfoForPanel(Long appointmentId) {
        List<Object[]> results = appointmentRepository.findAppointmentInfoForPanel(appointmentId);

        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        Object[] result = results.get(0); // ← importante
        return Optional.of(mapToDto(result));
    }

    private InfoAppointmentForPanelDTO mapToDto(Object[] result) {
        InfoAppointmentForPanelDTO dto = new InfoAppointmentForPanelDTO();

        // El orden debe coincidir exactamente con la consulta SQL
        dto.setIdAppointment(result[0] != null ? Long.valueOf(result[0].toString()) : null);
        dto.setTimeAppointment(result[1] != null ? result[1].toString() : null);
        dto.setComment(result[2] != null ? result[2].toString() : null);
        dto.setServiceId(result[3] != null ? Long.valueOf(result[3].toString()) : null);
        dto.setServiceTime(result[4] != null ? Integer.parseInt(result[4].toString()) : 0);
        dto.setServiceName(result[5] != null ? result[5].toString() : null);
        dto.setEmployeeId(result[6] != null ? Long.valueOf(result[6].toString()) : null); // 👈 corregido
        dto.setEmployeeName(result[7] != null ? result[7].toString() : null);
        dto.setEmployeeRole(result[8] != null ? result[8].toString() : null);

        return dto;
    }

    @Override
    public Optional<AnimalInfoForAppointmentDTO> getAnimalInfoByAppointmentId(Long appointmentId) {
        List<Object[]> result = appointmentRepository.findAnimalInfoByAppointmentId(appointmentId);

        if (result.isEmpty()) return Optional.empty();

        Object[] row = result.get(0);

        AnimalInfoForAppointmentDTO dto = new AnimalInfoForAppointmentDTO(
                row[0] != null ? Long.valueOf(row[0].toString()) : null,           // animalId
                row[1] != null ? row[1].toString() : null,                         // birthDate (formateado)
                row[2] != null ? row[2].toString() : null,                         // name
                row[3] != null ? row[3].toString() : null,                         // urlImage
                row[4] != null ? new BigDecimal(row[4].toString()) : null,        // weight
                row[5] != null ? row[5].toString() : null,                         // breedName
                row[6] != null ? row[6].toString() : null,                         // speciesName
                row[7] != null ? row[7].toString() : null                          // animalComment
        );

        return Optional.of(dto);
    }

    @Override
    public Optional<ClientInfoForAppointmentDTO> getClientInfoForAppointment(Long appointmentId) {
        List<Object[]>  result = appointmentRepository.findClientInfoByAppointmentId(appointmentId);

        if (result.isEmpty()) return Optional.empty();

        Object[] row = result.get(0);

        ClientInfoForAppointmentDTO dto = new ClientInfoForAppointmentDTO();
        dto.setClientId(row[0] != null ? row[0].toString() : null);
        dto.setFullName(row[1] != null ? row[1].toString() : null);
        dto.setPhoneNumber(row[2] != null ? row[2].toString() : null);
        dto.setEmail(row[3] != null ? row[3].toString() : null);
        dto.setAddress(row[4] != null ? row[4].toString() : null);

        return Optional.of(dto);
    }

    @Override
    public Optional<PaymentInfoForAppointmentDTO> getPaymentInfoByAppointmentId(Long appointmentId) {
        List<Object[]> result = appointmentRepository.findPaymentInfoByAppointmentId(appointmentId);
        if (result.isEmpty()) return Optional.empty();

        Object[] row = result.get(0);
        PaymentInfoForAppointmentDTO dto = new PaymentInfoForAppointmentDTO();

        dto.setPaymentId(row[0] != null ? Long.valueOf(row[0].toString()) : null);
        dto.setAmount(row[1] != null ? new BigDecimal(row[1].toString()) : null);
        dto.setServiceName(row[2] != null ? row[2].toString() : null);
        dto.setPaymentMethodId(row[3] != null ? Long.valueOf(row[3].toString()) : null);
        dto.setPaymentMethod(row[4] != null ? row[4].toString() : null);
        dto.setPaymentStatus(row[5] != null ? row[5].toString() : null);

        return Optional.of(dto);
    }

    @Override
    public AppointmentStatsForReceptionistDTO getStatsByDate(){
        String dateStr = LocalDate.now().toString(); // Obtener la fecha actual en formato yyyy-MM-dd
        List<Object[]> rows = appointmentRepository.getAppointmentStatsByDate(dateStr);
        if (rows.isEmpty()) {
            return new AppointmentStatsForReceptionistDTO(0, 0, 0);
        }

        Object[] row = rows.get(0);
        return new AppointmentStatsForReceptionistDTO(
                ((Number) row[0]).intValue(), // totalAppointments
                ((Number) row[1]).intValue(), // confirmedAppointments
                ((Number) row[2]).intValue()  // pendingAppointments
        );
    }



}
