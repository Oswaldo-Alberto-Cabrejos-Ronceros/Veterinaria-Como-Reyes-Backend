package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;
import com.veterinaria.veterinaria_comoreyes.mapper.PaymentMethodMapper;
import com.veterinaria.veterinaria_comoreyes.repository.PaymentMethodRepository;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentMethodService;
import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements IPaymentMethodService {


    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;
    private final FilterStatus filterStatus;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository,PaymentMethodMapper paymentMethodMapper, FilterStatus filterStatus) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.filterStatus = filterStatus;
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentMethodDTO getPaymentMethodById(Long id) {
        filterStatus.activeFilterStatus(true);
        PaymentMethod method = paymentMethodRepository.findByPaymentMethodIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));
        return paymentMethodMapper.maptoPaymentMethodDTO(method);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        filterStatus.activeFilterStatus(true);
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::maptoPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto) {
        filterStatus.activeFilterStatus(true);
        PaymentMethod entity = paymentMethodMapper.maptoPaymentMethod(dto);
        PaymentMethod saved = paymentMethodRepository.save(entity);
        return paymentMethodMapper.maptoPaymentMethodDTO(saved);
    }

    @Transactional
    @Override
    public PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO dto) {
        filterStatus.activeFilterStatus(true);
        PaymentMethod existing = paymentMethodRepository.findByPaymentMethodIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        PaymentMethod updated = paymentMethodRepository.save(existing);
        return paymentMethodMapper.maptoPaymentMethodDTO(updated);
    }

    @Transactional
    @Override
    public void deletePaymentMethod(Long id) {
        filterStatus.activeFilterStatus(true);
        PaymentMethod method = paymentMethodRepository.findByPaymentMethodIdAndStatusIsTrue(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));
        method.setStatus(false); // Inactivar
        paymentMethodRepository.save(method);
    }
}
