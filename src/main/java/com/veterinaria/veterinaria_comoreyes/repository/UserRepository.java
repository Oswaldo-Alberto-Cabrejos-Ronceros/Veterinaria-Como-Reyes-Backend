package com.veterinaria.veterinaria_comoreyes.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUserIdAndStatusTrue(Long id);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE users
        SET status = 0
        WHERE user_id = :userId
    """, nativeQuery = true)
    void blockUser(@Param("userId") Long userId);

}
