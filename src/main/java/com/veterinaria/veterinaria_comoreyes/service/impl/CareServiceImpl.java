package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Care.CareDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CareRequestDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Care.CreateCareFromAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import com.veterinaria.veterinaria_comoreyes.mapper.CareMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareServiceImpl implements ICareService {

    private final CareRepository careRepository;
    private final CareMapper careMapper;
    private final PaymentRepository paymentRepository;
    private final IAppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final IPaymentService paymentService;
    private final IHeadquarterVetServiceService headquarterVetServiceService;

    @Autowired
    public CareServiceImpl(CareRepository careRepository, CareMapper careMapper, PaymentRepository paymentRepository, IAppointmentService appointmentService, AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, IPaymentService paymentService, IHeadquarterVetServiceService headquarterVetServiceService) {
        this.careRepository = careRepository;
        this.careMapper = careMapper;
        this.paymentRepository = paymentRepository;
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.paymentService = paymentService;
        this.headquarterVetServiceService = headquarterVetServiceService;
    }

    @Override
    public CareDTO getCareById(Long id) {
        Care care = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attencion con id: " + id + " no encontrada"));
        return careMapper.toDTO(care);
    }

    @Override
    public List<CareDTO> getCareByAppointment(Long appointmentId) {
        return careRepository.findByAppointment_AppointmentId(appointmentId)
                .stream()
                .map(careMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CareDTO> getAllCares() {
        return careRepository.findAll()
                .stream()
                .map(careMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CareDTO createCare(CareDTO careDTO) {
        Care care = careMapper.toEntity(careDTO);
        care.setStatusCare(StatusCare.EN_CURSO);
        care.setCareDateTime(LocalDateTime.now());
        // ✅ SOLUCIÓN: Cargar el empleado desde la BD si viene el ID
        if (careDTO.getEmployeeId() != null) {
            Employee empleado = employeeRepository.findById(careDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + careDTO.getEmployeeId()));
            care.setEmployee(empleado);
        }
        if (careDTO.getAppointmentId() == null) {
            care.setAppointment(null); // Falta esto ❗
        }
        Care saved = careRepository.save(care);
        return careMapper.toDTO(saved);
    }

    @Override
    public CareDTO completeCare(Long id) {
        Care care = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));
        care.setStatusCare(StatusCare.COMPLETADO);
        Care updated = careRepository.save(care);
        return careMapper.toDTO(updated);
    }

    @Override
    public CareDTO updateCare(Long id, CareDTO careDTO) {
        Care existingCare = careRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));

        careMapper.updateEntityFromDto(careDTO, existingCare);
        Care updated = careRepository.save(existingCare);
        return careMapper.toDTO(updated);
    }

    @Override
    public UserBuyerDTO getInfoForPaymentMerPago(Long careId) {
        Payment payment = paymentRepository.findByCare_CareIdAndStatus(careId, "PENDIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("No hay pago pendiente para este care"));

        String title = payment.getCare()
                .getHeadquarterVetService()
                .getVeterinaryService()
                .getName();

        Long idOrderPayment = payment.getPaymentId();
        Integer quantity = 1;
        Double unitPrice = payment.getCare()
                .getHeadquarterVetService()
                .getVeterinaryService()
                .getPrice();

        UserBuyerDTO userBuyerDTO = new UserBuyerDTO();
        userBuyerDTO.setTitle(title);
        userBuyerDTO.setIdOrderPayment(idOrderPayment);
        userBuyerDTO.setQuantity(quantity);
        userBuyerDTO.setUnitPrice(unitPrice);

        return userBuyerDTO;
    }


    @Transactional
    @Override
    public CareDTO createCareFromAppointment(CreateCareFromAppointmentDTO dto) {
        // 1. Obtener la cita
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + dto.getAppointmentId()));

        // 2. Construir el CareDTO con la información desde Appointment
        CareDTO careDTO = new CareDTO();
        careDTO.setAppointmentId(appointment.getAppointmentId());
        careDTO.setAnimalId(appointment.getAnimal().getAnimalId());
        careDTO.setEmployeeId(dto.getEmployeeId());
        careDTO.setHeadquarterVetServiceId(appointment.getHeadquarterVetService().getId());
        careDTO.setDateTime(LocalDateTime.now()); // Fecha y hora actual de atención
        careDTO.setStatusCare(StatusCare.EN_CURSO); // Estado inicial

        // 3. Crear el registro de atención (Care)
        CareDTO createdCare = createCare(careDTO);

        Long careId = createdCare.getCareId();

        Long paymentId = paymentRepository.findPaymentIdByAppointmentId(appointment.getAppointmentId());

        paymentRepository.updateCareIdByPaymentId(paymentId, careId);

        appointmentService.completeAppointment(appointment.getAppointmentId());

        return createdCare;
    }

    @Transactional
    @Override
    public CareDTO createCareFromRequest(CareRequestDTO dto) {
        // 1. Armar el DTO del Care
        CareDTO careDTO = new CareDTO();
        careDTO.setAnimalId(dto.getAnimalId());
        careDTO.setEmployeeId(dto.getEmployeeId());
        careDTO.setHeadquarterVetServiceId(dto.getHeadquarterVetServiceId());
        careDTO.setDateTime(LocalDateTime.now());
        careDTO.setStatusCare(StatusCare.EN_CURSO);

        // 2. Crear y guardar el Care
        CareDTO createdCare = createCare(careDTO);

        System.out.println("ID generado: " + createdCare.getCareId());

        // 3. Obtener precio del servicio
        Double priceService = headquarterVetServiceService.priceService(dto.getHeadquarterVetServiceId());

        // 4. Crear DTO del Payment
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAppointmentId(null);
        paymentDTO.setPaymentMethodId(dto.getPaymentMethodId());
        paymentDTO.setStatus(PaymentStatus.PENDIENTE);
        paymentDTO.setAmount(priceService);
        paymentDTO.setPaymentDateTime(null); // Se puede dejar null si lo establece el service
        paymentDTO.setCareId(createdCare.getCareId());
        paymentDTO.setAppointmentId(null);

        // 5. Crear el pago desde el servicio
        paymentService.createPayment(paymentDTO);

        // 6. Retornar el Care creado
        return createdCare;
    }
    @Override
    public Page<CareListDTO> searchCares(String fecha, Long idHeadquarter, Long idService, String estado, Pageable pageable) {
        Page<Object[]> resultPage = careRepository.searchCaresNative(fecha, idHeadquarter, idService, estado, pageable);

        List<CareListDTO> content = resultPage.getContent().stream().map(row -> {
            CareListDTO dto = new CareListDTO();
            dto.setCareId(((Number) row[0]).longValue());
            dto.setCareDateTime((String) row[1]);
            dto.setStatusCare((String) row[2]);
            dto.setAnimalName((String) row[3]);
            dto.setAnimalSpecies((String) row[4]);
            dto.setAnimalBreed((String) row[5]);
            dto.setEmployeeFullName((String) row[6]);
            dto.setServiceName((String) row[7]);
            dto.setServicePrice(((Number) row[8]).doubleValue());
            dto.setHeadquarterName((String) row[9]);
            dto.setAppointmentId(row[10] != null ? ((Number) row[10]).longValue() : null);
            return dto;
        }).toList();

        return new PageImpl<>(content, pageable, resultPage.getTotalElements());
    }



    // @Override
    // public void deleteCare(Long id) {
    //     Care care = careRepository.findById(id)
    //             .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));
    //     careRepository.delete(care);
    // }
}
