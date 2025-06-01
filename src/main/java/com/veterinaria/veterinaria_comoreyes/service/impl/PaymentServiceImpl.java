package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.PaymentDTO;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.mapper.PaymentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final AppointmentRepository appointmentRepository;
    private final CareRepository careRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
            PaymentMapper paymentMapper,
            AppointmentRepository appointmentRepository,
            CareRepository careRepository,
            PaymentMethodRepository paymentMethodRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.appointmentRepository = appointmentRepository;
        this.careRepository = careRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);

        // Validaciones
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        payment.setAppointment(appointment);

        if (dto.getCareId() != null) {
            Care care = careRepository.findById(dto.getCareId())
                    .orElseThrow(() -> new RuntimeException("Atención no encontrada"));
            payment.setCare(care);
        }

        PaymentMethod method = paymentMethodRepository.findById(dto.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        payment.setPaymentMethod(method);

        // Validar estado inicial permitido
        if (dto.getStatus() == null || dto.getStatus() == PaymentStatus.RECHAZADA
                || dto.getStatus() == PaymentStatus.REEMBOLSADA) {
            payment.setStatus(PaymentStatus.PENDIENTE);
        } else {
            payment.setStatus(dto.getStatus());
        }

        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
        Payment existing = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        // No se permite cambiar a REEMBOLSADA directamente sin lógica aparte
        if (dto.getStatus() == PaymentStatus.REEMBOLSADA || dto.getStatus() == PaymentStatus.PARCIALMENTE_REEMBOLSADA) {
            throw new IllegalArgumentException("El estado de reembolso debe ser gestionado por un proceso especial.");
        }

        // Aplicar cambios permitidos
        paymentMapper.updateEntityFromDto(dto, existing);

        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
            existing.setAppointment(appointment);
        }

        if (dto.getCareId() != null) {
            Care care = careRepository.findById(dto.getCareId())
                    .orElseThrow(() -> new RuntimeException("Atención no encontrada"));
            existing.setCare(care);
        }

        if (dto.getPaymentMethodId() != null) {
            PaymentMethod method = paymentMethodRepository.findById(dto.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
            existing.setPaymentMethod(method);
        }

        return paymentMapper.toDTO(paymentRepository.save(existing));
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        // En lugar de eliminar, podrías marcarlo como CANCELADA
        payment.setStatus(PaymentStatus.CANCELADA);
        paymentRepository.save(payment);
    }
}
