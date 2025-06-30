package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.PaymentMethod;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
Optional<PaymentMethod> findByPaymentMethodIdAndStatusIsTrue(Long id);
    boolean existsByPaymentMethodIdAndStatusIsTrue(Long id);

    @Modifying
    @Query("UPDATE PaymentMethod p SET p.status = true WHERE p.paymentMethodId = :paymentMethodId")
    void activatePaymentMethod(@Param("paymentMethodId") Long paymentMethodId);
}
