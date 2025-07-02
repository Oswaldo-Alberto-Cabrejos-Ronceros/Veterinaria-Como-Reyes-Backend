package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryIdAndStatusIsTrue(Long id);

    @Modifying
    @Query("UPDATE Category c SET c.status = true WHERE c.categoryId = :categoryId")
    void activateCategory(@Param("categoryId") Long categoryId);
}
