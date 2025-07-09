package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Payment.*;
import com.veterinaria.veterinaria_comoreyes.entity.*;
import com.veterinaria.veterinaria_comoreyes.mapper.PaymentMapper;
import com.veterinaria.veterinaria_comoreyes.repository.*;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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


        if (dto.getCareId() != null) {
            Care care = careRepository.findById(dto.getCareId())
                    .orElseThrow(() -> new RuntimeException("Atención no encontrada"));
            payment.setCare(care);
        }else{
            payment.setCare(null);
        }

        // Validar y asignar cita si corresponde
        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
            payment.setAppointment(appointment);
        } else {
            payment.setAppointment(null);
        }

        PaymentMethod method = paymentMethodRepository.findById(dto.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        payment.setPaymentMethod(method);

        // Validar estado inicial permitido
        if (dto.getStatus() == null) {
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
        // Validar que el pago no esté en un estado que impida modificaciones
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

    @Override
    public Page<PaymentListDTO> getAllPaymentsForTable(Pageable pageable) {
        Page<Object[]> results = paymentRepository.findAllPaymentsForTable(pageable);
        return results.map(row -> {
            PaymentListDTO dto = new PaymentListDTO();
            dto.setId(((Number) row[0]).longValue());
            dto.setHeadquarterName((String) row[1]);
            dto.setServiceName((String) row[2]);
            dto.setClientDni((String) row[3]);
            dto.setAmount(BigDecimal.valueOf(((Number) row[4]).doubleValue()));
            dto.setStatus((String) row[5]);
            dto.setPaymentMethod((String) row[6]);
            dto.setPaymentDate((String) row[7]);
            return dto;
        });
    }

    @Override
    public Page<PaymentListDTO> searchPayments(String dni, Long headquarterId, Long serviceId,
                                               String status, String startDate, String endDate,
                                               Pageable pageable) {
        Page<Object[]> pag = paymentRepository.searchPaymentsForTable(dni, headquarterId, serviceId,
                status, startDate, endDate, pageable);
        return pag.map(row -> {
            PaymentListDTO dto = new PaymentListDTO();
            dto.setId(((Number) row[0]).longValue());
            dto.setHeadquarterName((String) row[1]);
            dto.setServiceName((String) row[2]);
            dto.setClientDni((String) row[3]);
            dto.setAmount(BigDecimal.valueOf(((Number) row[4]).doubleValue()));
            dto.setStatus((String) row[5]);
            dto.setPaymentMethod((String) row[6]);
            dto.setPaymentDate((String) row[7]);
            return dto;
        });
    }

    @Transactional
    @Override
    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        paymentRepository.updateStatus(paymentId, status);
    }

    @Override
    public void updatePaymentStatusToCompleted(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("No se encontró el pago con ID: " + paymentId));

        payment.setStatus(PaymentStatus.COMPLETADA);
        payment.setPaymentDateTime(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    @Override
    public PaymentStatsForPanelAdminDTO getCompletedPaymentsStats() {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate previousMonthDate = today.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear();

        Object[] row = (Object[]) paymentRepository.getCompletedPaymentStats(
                currentMonth, currentYear, previousMonth, previousYear
        );

        double currentTotal = ((Number) row[0]).doubleValue();
        double previousTotal = ((Number) row[1]).doubleValue();

        double percentageDiff = 0;
        String percentageFormatted = "0%";

        if (previousTotal > 0) {
            percentageDiff = ((currentTotal - previousTotal) / previousTotal) * 100;
            percentageFormatted = String.format("%s%.2f%%",
                    percentageDiff >= 0 ? "+" : "", percentageDiff);
        }

        return new PaymentStatsForPanelAdminDTO(currentTotal, previousTotal, percentageFormatted);
    }

    @Override
    public PaymentStatsForPanelAdminDTO getPaymentsStatsByHeadquarter(Long headquarterId) {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate previousMonthDate = today.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear();

        Object[] row = (Object[]) paymentRepository.getPaymentsStatsByHeadquarter(
                currentMonth, currentYear, previousMonth, previousYear, headquarterId);

        double currentTotal = ((Number) row[0]).doubleValue();
        double previousTotal = ((Number) row[1]).doubleValue();

        String percentageDifference;
        if (previousTotal == 0 && currentTotal > 0) {
            percentageDifference = "+100%";
        } else if (previousTotal == 0) {
            percentageDifference = "0%";
        } else {
            double diff = ((currentTotal - previousTotal) / previousTotal) * 100;
            percentageDifference = (diff >= 0 ? "+" : "") + String.format("%.2f", diff) + "%";
        }

        return new PaymentStatsForPanelAdminDTO(currentTotal, previousTotal, percentageDifference);
    }

    @Override
    public IncomeStatsTodayDTO getTodayIncomeStats() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        BigDecimal income = paymentRepository.getTodayIncome(today);
        return new IncomeStatsTodayDTO(income);
    }
    @Override
    public List<RecentPaymentsDTO> getRecentCompletedPayments(Long headquarterId) {
        List<Object[]> rows = paymentRepository.findRecentCompletedPaymentsByHeadquarter(headquarterId);

        return rows.stream().map(row -> new RecentPaymentsDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4],
                new BigDecimal(((Number) row[5]).doubleValue()),
                (String) row[6],
                (String) row[7],
                (String) row[8]
        )).collect(Collectors.toList());
    }

    @Override
    public WeeklyIncomeDTO getWeeklyIncomeGeneral() {
        String startDate = getStartOfWeekDate();
        List<Object[]> rows = paymentRepository.getWeeklyIncomeGeneral(startDate);
        return mapToDTO(rows);
    }

    @Override
    public WeeklyIncomeDTO getWeeklyIncomeByHeadquarter(Long headquarterId) {
        String startDate = getStartOfWeekDate();
        List<Object[]> rows = paymentRepository.getWeeklyIncomeByHeadquarter(startDate, headquarterId);
        return mapToDTO(rows);
    }

    private String getStartOfWeekDate() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        return start.toString(); // Formato yyyy-MM-dd
    }

    private WeeklyIncomeDTO mapToDTO(List<Object[]> rows) {
        List<String> days = new ArrayList<>();
        List<BigDecimal> totals = new ArrayList<>();

        for (Object[] row : rows) {
            days.add(((String) row[0]).strip());
            totals.add((BigDecimal) row[1]);
        }

        return new WeeklyIncomeDTO(days, totals);
    }

}
