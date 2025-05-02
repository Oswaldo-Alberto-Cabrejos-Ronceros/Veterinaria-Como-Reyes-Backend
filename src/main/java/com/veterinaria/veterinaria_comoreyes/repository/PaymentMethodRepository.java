package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
Optional<PaymentMethod> findByPaymentMethodIdAndStatusIsTrue(Long id);
}
