package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientListDTO;
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

    @Query("SELECT new com.veterinaria.veterinaria_comoreyes.dto.Client.ClientListDTO(c.clientId, c.dni, c.name, c.lastName, c.headquarter.name,CASE WHEN c.status = true THEN 'Activo' ELSE 'Bloqueado' END) " +
            "FROM Client c WHERE " +
            "(:dni IS NULL OR c.dni = :dni) AND " +
            "(:name IS NULL OR c.name LIKE %:name%) AND " +
            "(:lastName IS NULL OR c.lastName LIKE %:lastName%) AND " +
            "(:status IS NULL OR c.status = :status) AND " +
            "(:headquarterId IS NULL OR c.headquarter.headquarterId = :headquarterId)")
    Page<ClientListDTO> searchClients(@Param("dni") String dni,
                                      @Param("name") String name,
                                      @Param("lastName") String lastName,
                                      @Param("status") Boolean status,
                                      @Param("headquarterId") Long headquarterId,
                                      Pageable pageable);

    Client findByClientId(long clientId);

    Optional<Client> findByClientIdAndStatusIsTrue(Long Id);
}
