package com.veterinaria.veterinaria_comoreyes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryListDTO;
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

    @Query("""
                SELECT new com.veterinaria.veterinaria_comoreyes.dto.Category.CategoryListDTO(
                    c.categoryId,
                    c.name,
                    CASE WHEN c.status = true THEN 'Activo' ELSE 'Inactivo' END
                )
                FROM Category c
                WHERE (:name IS NULL OR c.name LIKE %:name%) AND
                      (:status IS NULL OR c.status = :status)
            """)
    Page<CategoryListDTO> searchCategoriesWithFilters(
            @Param("name") String name,
            @Param("status") Boolean status,
            Pageable pageable);
}
