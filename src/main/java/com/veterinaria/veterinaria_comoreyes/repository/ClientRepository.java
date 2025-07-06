package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Client.ClientListDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    Boolean existsByClientIdAndStatusIsTrue(Long clientId);

    @Query(value = """
        SELECT c.client_id, c.name || ' ' || c.last_name AS full_name
        FROM client c
        WHERE c.dni = :dni
        """, nativeQuery = true)
    Object findBasicInfoByDni(@Param("dni") String dni);

    @Query(value = """
    SELECT 
        client_id,
        INITCAP(
            REGEXP_SUBSTR(name, '^\\S+') || ' ' || REGEXP_SUBSTR(last_name, '^\\S+')
        ) AS full_name,
        UPPER(SUBSTR(name, 1, 1) || SUBSTR(last_name, 1, 1)) AS initials,
        phone
    FROM 
        client
    WHERE 
        status = 1
    ORDER BY 
        client_id DESC
""", nativeQuery = true)
    List<Object[]> findClientInfoPanelAdminRaw();

    @Query(value = """
    SELECT 
        client_id,
        INITCAP(
            REGEXP_SUBSTR(name, '^\\S+') || ' ' || REGEXP_SUBSTR(last_name, '^\\S+')
        ) AS full_name,
        UPPER(SUBSTR(name, 1, 1) || SUBSTR(last_name, 1, 1)) AS initials,
        phone
    FROM 
        client
    WHERE 
        id_headquarter = :headquarterId
        AND status = 1
    ORDER BY 
        client_id DESC
""", nativeQuery = true)
    List<Object[]> findClientInfoPanelByHeadquarterId(@Param("headquarterId") Long headquarterId);

    @Query(value = """
    SELECT 
        COUNT(*) AS total_clients,
        SUM(CASE 
                WHEN EXTRACT(MONTH FROM create_date) = :month
                 AND EXTRACT(YEAR FROM create_date) = :year
                THEN 1 ELSE 0 
            END) AS current_month,
        SUM(CASE 
                WHEN EXTRACT(MONTH FROM create_date) = :previousMonth
                 AND EXTRACT(YEAR FROM create_date) = :previousYear
                THEN 1 ELSE 0 
            END) AS previous_month
    FROM client
""", nativeQuery = true)
    List<Object[]>  getClientStatsPanel(
            @Param("month") int currentMonth,
            @Param("year") int currentYear,
            @Param("previousMonth") int previousMonth,
            @Param("previousYear") int previousYear
    );

    @Query(value = """
    SELECT 
        COUNT(*) AS total_clients,
        SUM(CASE 
                WHEN EXTRACT(MONTH FROM cl.create_date) = :month
                 AND EXTRACT(YEAR FROM cl.create_date) = :year
                THEN 1 ELSE 0 
            END) AS current_month,
        SUM(CASE 
                WHEN EXTRACT(MONTH FROM cl.create_date) = :previousMonth
                 AND EXTRACT(YEAR FROM cl.create_date) = :previousYear
                THEN 1 ELSE 0 
            END) AS previous_month
    FROM client cl
    WHERE cl.id_headquarter = :headquarterId
""", nativeQuery = true)
    List<Object[]>  getClientStatsByHeadquarterPanel(
            @Param("month") int currentMonth,
            @Param("year") int currentYear,
            @Param("previousMonth") int previousMonth,
            @Param("previousYear") int previousYear,
            @Param("headquarterId") Long headquarterId
    );





}
