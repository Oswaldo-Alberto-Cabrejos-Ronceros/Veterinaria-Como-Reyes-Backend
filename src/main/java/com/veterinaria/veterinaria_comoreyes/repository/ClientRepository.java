package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUser(User user);

    boolean existsByPhone(String phone);

    boolean existsByDni(String dni);

    @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Specie.SpecieDTO(" +
            "s.specieId, s.name, s.imagePath, s.status) " +
            "FROM Specie s WHERE " +
            "(:name IS NULL OR s.name LIKE %:name%) AND " +
            "(:imagePath IS NULL OR s.imagePath LIKE %:imagePath%) AND " +
            "(:status IS NULL OR s.status = :status)")
    Page<SpecieDTO> searchSpecies(@Param("name") String name,
            @Param("imagePath") String imagePath,
            @Param("status") Boolean status,
            Pageable pageable);

    Client findByClientId(long clientId);

    Optional<Client> findByClientIdAndStatusIsTrue(Long Id);

    Boolean existsByClientIdAndStatusIsTrue(Long clientId);
}
