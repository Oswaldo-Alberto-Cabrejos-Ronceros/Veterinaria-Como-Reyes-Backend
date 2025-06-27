package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.dto.Appointment.BasicServiceForAppointmentDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Appointment.FormatTimeDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import com.veterinaria.veterinaria_comoreyes.external.mercadoPago.dto.UserBuyerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    Boolean existsByAnimalAndScheduleDateTimeAndStatusAppointment(Animal animal, LocalDateTime scheduleDateTime, StatusAppointment statusAppointment);

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
    Boolean isAppointmentSlotAvailable(LocalDateTime scheduleDateTime,Long headquarterServiceId);

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
          SELECT 
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
          AND h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE') < h.end_time
          AND NVL(ap.total_citas, 0) < s.simultaneous_capacity
        ORDER BY h.start_time + NUMTODSINTERVAL((gen.lvl - 1) * s.duration, 'MINUTE')
    """, nativeQuery = true)
    List<FormatTimeDTO> timesAvailableForServiceAndDate(
            @Param("headquarterServiceId") Long headquarterServiceId,
            @Param("fechaSeleccionada") String fechaSeleccionada
    );

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













}
