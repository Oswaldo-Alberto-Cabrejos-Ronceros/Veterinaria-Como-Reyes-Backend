package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.BasicServiceForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.FormatTimeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Buscar por ID con estado lógico TRUE
    Optional<Appointment> findByAppointmentId(Long id);

    // Buscar todas las citas activas por animal
    List<Appointment> findByAnimal(Animal animal);

    // Buscar todas las citas activas por empleado asignado
    List<Appointment> findByEmployee(Employee employee);

    // Buscar citas por estado específico
    List<Appointment> findByStatusAppointment(StatusAppointment statusAppointment);

    // Buscar citas de un día específico}
    List<Appointment> findByScheduleDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Validar si ya existe una cita programada para un animal en una fecha/hora
    Boolean existsByAnimalAndScheduleDateTimeAndStatusAppointment(Animal animal, LocalDateTime scheduleDateTime,
            StatusAppointment statusAppointment);

    // Buscar citas en un rango de fechas con un estado específico
    List<Appointment> findByScheduleDateTimeBetweenAndStatusAppointment(
            LocalDateTime start, LocalDateTime end, StatusAppointment status);

    // Nuevo método con fetch de relaciones
    @Query("SELECT a FROM Appointment a " +
            "LEFT JOIN FETCH a.animal ani " +
            "LEFT JOIN FETCH ani.client cli " +
            "LEFT JOIN FETCH cli.user usr " +
            "LEFT JOIN FETCH a.headquarterVetService hvs " +
            "LEFT JOIN FETCH hvs.veterinaryService " +
            "LEFT JOIN FETCH hvs.headquarter " +
            "WHERE a.appointmentId = :id")
    Optional<Appointment> findByIdWithRelations(@Param("id") Long id);

    @Query("""
            SELECT CASE WHEN (
                    SELECT COUNT(a) FROM Appointment a
                    WHERE a.headquarterVetService.id = :headquarterServiceId
                    AND a.scheduleDateTime = :scheduleDateTime
                    AND a.statusAppointment IN ('PROGRAMADA', 'CONFIRMADA')
                ) < (
                    SELECT hvs.veterinaryService.simultaneousCapacity FROM HeadquarterVetService hvs WHERE hvs.id = :headquarterServiceId
                )
                THEN true ELSE false END
            """)
    Boolean isAppointmentSlotAvailable(LocalDateTime scheduleDateTime, Long headquarterServiceId);

    @Query(value = """
                SELECT
                    TO_CHAR(h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE'), 'HH24:MI') AS time,
                    TO_CHAR(h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE'), 'HH24:MI') || ' - ' ||
                    TO_CHAR(h.start_time + NUMTODSINTERVAL(gen.lvl * s.duration, 'MINUTE'), 'HH24:MI') AS timeRange
                FROM headquarter_vet_service hs
                JOIN headquarter h ON hs.id_headquarter = h.headquarter_id
                JOIN veterinary_service s ON hs.id_service = s.service_id
                JOIN (SELECT LEVEL AS lvl FROM dual CONNECT BY LEVEL <= 100) gen ON 1 = 1
                LEFT JOIN (
                    SELECT\s
                        headquarter_vetservice_id,
                        TO_CHAR(schedule_date_time, 'HH24:MI') AS hora_clave_str,
                        COUNT(*) AS total_citas
                    FROM appointment
                    WHERE status_appointments IN ('PROGRAMADA', 'CONFIRMADA')
                      AND TRUNC(schedule_date_time) = TO_DATE(:fechaSeleccionada, 'YYYY-MM-DD')
                    GROUP BY headquarter_vetservice_id, TO_CHAR(schedule_date_time, 'HH24:MI')
                ) ap ON ap.headquarter_vetservice_id = hs.id
                     AND ap.hora_clave_str = TO_CHAR(h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE'), 'HH24:MI')
                WHERE hs.id = :headquarterServiceId
                  AND (h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE')) < h.end_time
                  AND NVL(ap.total_citas, 0) < hs.simultaneous_capacity
                  AND (
                    TO_DATE(:fechaSeleccionada, 'YYYY-MM-DD') > TRUNC(SYSDATE)
                    OR (
                      TO_DATE(:fechaSeleccionada, 'YYYY-MM-DD') = TRUNC(SYSDATE)
                      AND TO_CHAR(h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE'), 'HH24:MI:SS') > TO_CHAR(SYSDATE, 'HH24:MI:SS')
                    )
                  )
                ORDER BY h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE')

            """, nativeQuery = true)
    List<FormatTimeDTO> timesAvailableForServiceAndDate(
            @Param("headquarterServiceId") Long headquarterServiceId,
            @Param("fechaSeleccionada") String fechaSeleccionada);

    @Query(value = """
            SELECT
                hvs.id AS headquarterServiceId,
                vs.service_id AS serviceId,
                vs.name,
                vs.description,
                vs.price,
                vs.duration,
                s.name AS specieName,
                vs.dir_image AS serviceImageUrl,
                c.name AS categoryName
            FROM
                HEADQUARTER_VET_SERVICE hvs
            JOIN
                VETERINARY_SERVICE vs ON hvs.id_service = vs.service_id
            JOIN
                SPECIE s ON vs.id_specie = s.specie_id
            JOIN
                CATEGORY c ON vs.id_category = c.category_id
            WHERE
                hvs.id_headquarter = :headquarterId
                AND vs.id_specie = :speciesId
            """, nativeQuery = true)
    List<Object[]> findServiceDetailsForAppointment(@Param("headquarterId") Long headquarterId,
            @Param("speciesId") Long speciesId);

    @Query(value = """
                SELECT
                    a.appointment_id AS id,
                    TO_CHAR(a.schedule_date_time, 'YYYY-MM-DD') AS appointment_date,
                    TO_CHAR(a.schedule_date_time, 'HH24:MI') AS appointment_time,
                    an.name AS animalName,
                    s.name AS serviceName,
                    s.description AS serviceDescription,
                    s.dir_image AS serviceImage,
                    cs.name AS categoryServiceName,
                    a.status_appointments AS status,
                    s.duration AS duration
                FROM appointment a
                JOIN animal an ON a.animal_id = an.animal_id
                JOIN headquarter_vet_service hs ON a.headquarter_vetservice_id = hs.id
                JOIN veterinary_service s ON hs.id_service = s.service_id
                JOIN category cs ON s.id_category = cs.category_id
                WHERE an.client_id = :clientId
                  AND a.status_appointments IN ('CONFIRMADA', 'PROGRAMADA')
                ORDER BY a.schedule_date_time ASC
            """, nativeQuery = true)
    List<Object[]> findInfoBasicAppointmentsByClientId(@Param("clientId") Long clientId);

    @Query("""
            SELECT new com.veterinaria.veterinaria_comoreyes.dto.Appointment.AppointmentListDTO(
                a.appointmentId,
                TO_CHAR(a.scheduleDateTime, 'YYYY-MM-DD'),
                h.name,
                c.name,
                a.statusAppointment
            )
            FROM Appointment a
            JOIN a.headquarterVetService hvs
            JOIN hvs.headquarter h
            JOIN hvs.veterinaryService vs
            JOIN vs.category c
            WHERE (:day IS NULL OR TO_CHAR(a.scheduleDateTime, 'YYYY-MM-DD') = :day)
            AND (:headquarter IS NULL OR h.name LIKE %:headquarter%)
            AND (:categoryService IS NULL OR c.name LIKE %:categoryService%)
            AND (:appointmentStatus IS NULL OR a.statusAppointment = :appointmentStatus)
            ORDER BY a.appointmentId DESC
            """)
    Page<AppointmentListDTO> searchAppointments(
            @Param("day") String day,
            @Param("headquarter") String headquarter,
            @Param("categoryService") String categoryService,
            @Param("appointmentStatus") StatusAppointment appointmentStatus,
            Pageable pageable);

    @Query(value = """
                SELECT
                    a.appointment_id,
                    an.name AS animal_name,
                    vs.name AS service_name,
                    INITCAP(REGEXP_SUBSTR(cl.name, '^\\S+')) || ' ' || INITCAP(REGEXP_SUBSTR(cl.last_name, '^\\S+')) AS client_name,
                    TO_CHAR(a.schedule_date_time, 'HH24:MI') AS hour,
                    a.status_appointments AS status
                FROM appointment a
                JOIN animal an ON a.animal_id = an.animal_id
                JOIN client cl ON an.client_id = cl.client_id
                JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                WHERE TRUNC(a.schedule_date_time) = :date
                AND a.status_appointments IN ('PROGRAMADA', 'CONFIRMADA')
                ORDER BY a.schedule_date_time ASC
            """, nativeQuery = true)
    List<Object[]> getAppointmentsInfoByDate(@Param("date") LocalDate date);

    @Query(value = """
                SELECT
                    a.appointment_id,
                    an.name AS animal_name,
                    vs.name AS service_name,
                    INITCAP(REGEXP_SUBSTR(cl.name, '^\\S+')) || ' ' || INITCAP(REGEXP_SUBSTR(cl.last_name, '^\\S+')) AS client_name,
                    TO_CHAR(a.schedule_date_time, 'HH24:MI') AS hour,
                    a.status_appointments AS status
                FROM appointment a
                JOIN animal an ON a.animal_id = an.animal_id
                JOIN client cl ON an.client_id = cl.client_id
                JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                JOIN veterinary_service vs ON hvs.id_service = vs.service_id
                WHERE TRUNC(a.schedule_date_time) = :date
                AND a.status_appointments IN ('PROGRAMADA', 'CONFIRMADA')
                AND hvs.id_headquarter = :headquarterId
                ORDER BY a.schedule_date_time ASC
            """, nativeQuery = true)
    List<Object[]> getAppointmentsInfoByDateAndHeadquarter(
            @Param("date") LocalDate date,
            @Param("headquarterId") Long headquarterId);

    @Query(value = """
                SELECT
                    COUNT(*) AS total,
                    NVL(SUM(CASE
                        WHEN TRUNC(creation_date) = TRUNC(SYSDATE)
                         AND TRUNC(schedule_date_time) = TRUNC(SYSDATE)
                        THEN 1 ELSE 0
                    END), 0) AS today_appointments
                FROM appointment
                WHERE TRUNC(schedule_date_time) = TRUNC(SYSDATE)
                  AND status_appointments IN ('PROGRAMADA', 'CONFIRMADA', 'COMPLETADA')
            """, nativeQuery = true)
    List<Object[]> getTodayAppointmentStats();

    @Query(value = """
                SELECT
                    COUNT(*) AS total,
                    NVL(SUM(CASE
                        WHEN TRUNC(a.creation_date) = TRUNC(SYSDATE)
                         AND TRUNC(a.schedule_date_time) = TRUNC(SYSDATE)
                        THEN 1 ELSE 0
                    END), 0) AS today_appointments
                FROM appointment a
                JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
                WHERE TRUNC(a.schedule_date_time) = TRUNC(SYSDATE)
                  AND a.status_appointments IN ('PROGRAMADA', 'CONFIRMADA', 'COMPLETADA')
                  AND hvs.id_headquarter = :headquarterId
            """, nativeQuery = true)
    List<Object[]> getTodayAppointmentStatsByHeadquarter(@Param("headquarterId") Long headquarterId);

    @Query(value = """
    SELECT
        a.appointment_id AS id,
        'CITA' AS type,
        an.animal_id AS animal_id,
        an.name AS animal_name,
        vs.name AS service_name,
        cl.name || ' ' || cl.last_name AS client_name,
        TO_CHAR(a.schedule_date_time, 'YYYY-MM-DD') AS fecha,
        TO_CHAR(a.schedule_date_time, 'HH24:MI') AS hora,
        a.status_appointments AS status,
        a.comentario,
        a.employee_id AS employee_id,
        e.name || ' ' || e.last_name AS employee_full_name,
        br.name AS breed_name
    FROM appointment a
    JOIN animal an ON an.animal_id = a.animal_id
    JOIN breed br ON br.breed_id = an.breed_id
    JOIN client cl ON cl.client_id = an.client_id
    JOIN headquarter_vet_service hvs ON hvs.id = a.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    JOIN employee e ON e.employee_id = a.employee_id
    WHERE TRIM(a.status_appointments) IN ('PROGRAMADA', 'CONFIRMADA')
      AND a.employee_id = :employeeId
    ORDER BY fecha ASC, hora ASC
    """, nativeQuery = true)
    List<Object[]> findAppointmentsByEmployeeId(@Param("employeeId") Long employeeId);



    @Query(nativeQuery = true, value = """
        SELECT 
            a.appointment_id, 
            TO_CHAR(a.schedule_date_time, 'HH24:MI'), 
            a.comentario, 
            vs.service_id, 
            vs.duration, 
            vs.name, 
            e.employee_id, 
            CASE 
                WHEN e.employee_id IS NULL THEN NULL
                ELSE e.name || ' ' || e.last_name
            END,
            CASE
                WHEN e.employee_id IS NULL THEN NULL
                WHEN EXISTS (
                    SELECT 1 FROM employee_role er
                    JOIN role r ON r.role_id = er.id_role
                    WHERE er.id_employee = e.employee_id AND UPPER(r.name) = 'VETERINARIO'
                ) THEN 'VETERINARIO'
                WHEN EXISTS (
                    SELECT 1 FROM employee_role er
                    JOIN role r ON r.role_id = er.id_role
                    WHERE er.id_employee = e.employee_id AND UPPER(r.name) = 'PRACTICANTE'
                ) THEN 'PRACTICANTE'
                ELSE 'ASISTENTE'
            END
        FROM appointment a 
        JOIN headquarter_vet_service hvs ON hvs.id = a.headquarter_vetservice_id 
        JOIN veterinary_service vs ON vs.service_id = hvs.id_service 
        LEFT JOIN employee e ON e.employee_id = a.employee_id 
        WHERE a.appointment_id = :appointmentId
        """)
    List<Object[]> findAppointmentInfoForPanel(@Param("appointmentId") Long appointmentId);

    @Query(value = """
    SELECT 
        an.animal_id,
        TO_CHAR(an.birth_date, 'dd/MM/yyyy') AS birth_date,
        an.name,
        an.url_image,
        an.weight,
        b.name AS breed_name,
        s.name AS species_name,
        an.animal_comment
    FROM appointment a
    JOIN animal an ON a.animal_id = an.animal_id
    JOIN breed b ON an.breed_id = b.breed_id
    JOIN specie s ON b.id_specie = s.specie_id
    WHERE a.appointment_id = :appointmentId
    """, nativeQuery = true)
    List<Object[]> findAnimalInfoByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query(value = """
        SELECT 
            cl.client_id,
            cl.name || ' ' || cl.last_name AS full_name,
            cl.phone,
            u.email,
            cl.address
        FROM appointment a
        JOIN animal an ON a.animal_id = an.animal_id
        JOIN client cl ON cl.client_id = an.client_id
        LEFT JOIN users u ON u.user_id = cl.id_user
        WHERE a.appointment_id = :appointmentId
        """, nativeQuery = true)
    List<Object[]> findClientInfoByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query(value = """
    SELECT 
        p.payment_id,
        p.amount,
        vs.name AS service_name,
        pm.payment_method_id,
        pm.name AS payment_method,
        p.status
    FROM payment p
    JOIN appointment a ON a.appointment_id = p.appointment_id
    JOIN headquarter_vet_service hvs ON hvs.id = a.headquarter_vetservice_id
    JOIN veterinary_service vs ON vs.service_id = hvs.id_service
    JOIN payment_method pm ON pm.payment_method_id = p.payment_method_id
    WHERE p.appointment_id = :appointmentId
""", nativeQuery = true)
    List<Object[]> findPaymentInfoByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query(value = """
    SELECT 
        COUNT(*) AS totalAppointments,
        COUNT(CASE WHEN a.status_appointments = 'CONFIRMADA' THEN 1 END) AS confirmedAppointments,
        COUNT(CASE WHEN a.status_appointments = 'PROGRAMADA' THEN 1 END) AS pendingAppointments
    FROM appointment a
    JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
    WHERE TRUNC(a.schedule_date_time) = TO_DATE(:dateStr, 'yyyy-MM-dd')
      AND hvs.id_headquarter = :headquarterId
    """, nativeQuery = true)
    List<Object[]> getAppointmentStatsByDateAndHeadquarter(
            @Param("dateStr") String dateStr,
            @Param("headquarterId") Long headquarterId
    );


    @Query(value = """
    SELECT
            a.appointment_id AS id,
            'CITA' AS type,
            an.animal_id AS animal_id,
            an.name AS animal_name,
            vs.name AS service_name,
            cl.name || ' ' || cl.last_name AS client_name,
            TO_CHAR(a.schedule_date_time, 'YYYY-MM-DD') AS fecha,
            TO_CHAR(a.schedule_date_time, 'HH24:MI') AS hora,
            a.status_appointments AS status,
            a.comentario,
            a.employee_id AS employeeId,
            e.name || ' ' || e.last_name AS employeeName,
            br.name AS breedName
        FROM appointment a
        JOIN animal an ON an.animal_id = a.animal_id
        JOIN breed br ON an.breed_id = br.breed_id
        JOIN client cl ON cl.client_id = an.client_id
        JOIN headquarter_vet_service hvs ON hvs.id = a.headquarter_vetservice_id
        JOIN veterinary_service vs ON vs.service_id = hvs.id_service
        JOIN employee e ON e.employee_id = a.employee_id
        WHERE TRIM(a.status_appointments) IN ('PROGRAMADA', 'CONFIRMADA')
          AND hvs.id_headquarter = :headquarterId
        ORDER BY fecha ASC, hora ASC
""", nativeQuery = true)
    List<Object[]> findAppointmentsByHeadquarterId(@Param("headquarterId") Long headquarterId);

}
