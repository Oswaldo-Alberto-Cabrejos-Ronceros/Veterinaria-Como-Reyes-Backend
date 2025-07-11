package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodListDTO;
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

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Payment_Method.PaymentMethodListDTO(
                    p.paymentMethodId,
                    p.name,
                    p.description
                )
                FROM PaymentMethod p
                WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT(:name, '%')))
                  AND (:status IS NULL OR p.status = :status)
                ORDER BY p.paymentMethodId DESC
            """)
    Page<PaymentMethodListDTO> searchPaymentMethodsWithFilters(
            @Param("name") String name,
            @Param("status") Boolean status,
            Pageable pageable);

}
