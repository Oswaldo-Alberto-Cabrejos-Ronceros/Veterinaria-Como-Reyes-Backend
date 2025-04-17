package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.PaymentMethodDTO;
import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;
import com.veterinaria.veterinaria_comoreyes.mapper.PaymentMethodMapper;
import com.veterinaria.veterinaria_comoreyes.repository.PaymentMethodRepository;
import com.veterinaria.veterinaria_comoreyes.service.IPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements IPaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public PaymentMethodDTO getPaymentMethodById(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));
        return PaymentMethodMapper.maptoPaymentMethodDTO(method);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(PaymentMethodMapper::maptoPaymentMethodDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto) {
        PaymentMethod entity = PaymentMethodMapper.maptoPaymentMethod(dto);
        PaymentMethod saved = paymentMethodRepository.save(entity);
        return PaymentMethodMapper.maptoPaymentMethodDTO(saved);
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO dto) {
        PaymentMethod existing = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());

        PaymentMethod updated = paymentMethodRepository.save(existing);
        return PaymentMethodMapper.maptoPaymentMethodDTO(updated);
    }

    @Override
    public void deletePaymentMethod(Long id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found with id: " + id));
        method.setStatus(0); // Inactivar
        paymentMethodRepository.save(method);
    }
}
