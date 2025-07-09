package com.veterinaria.veterinaria_comoreyes.external.reports.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.AnimalsByTypeDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.AppointmentsByTimeDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.AppointmentsByVetAndPeriodDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.AppointmentsByVetDTO;
import com.veterinaria.veterinaria_comoreyes.external.reports.clinic.dto.PopularServicesDTO;

public interface ClinicReportRepository extends JpaRepository<Appointment, Long> {

    @Query(value = """
                SELECT TO_CHAR(a.schedule_date_time, 'YYYY-MM-DD') AS period, COUNT(*) AS count
                FROM appointment a
                WHERE a.status_appointments = 'COMPLETADA'
                GROUP BY TO_CHAR(a.schedule_date_time, 'YYYY-MM-DD')
                ORDER BY period
            """, nativeQuery = true)
    List<Object[]> findDailyAppointments();

    @Query(value = """
                SELECT TO_CHAR(a.schedule_date_time, 'YYYY-IW') AS period, COUNT(*) AS count
                FROM appointment a
                WHERE a.status_appointments = 'COMPLETADA'
                GROUP BY TO_CHAR(a.schedule_date_time, 'YYYY-IW')
                ORDER BY period
            """, nativeQuery = true)
    List<Object[]> findWeeklyAppointments();

    @Query(value = """
                SELECT TO_CHAR(a.schedule_date_time, 'MM/YYYY') AS period, COUNT(*) AS count
                FROM appointment a
                WHERE a.status_appointments = 'COMPLETADA'
                GROUP BY TO_CHAR(a.schedule_date_time, 'MM/YYYY')
                ORDER BY period
            """, nativeQuery = true)
    List<Object[]> findMonthlyAppointments();

    @Query(value = """
                SELECT TO_CHAR(a.schedule_date_time, 'YYYY') AS period, COUNT(*) AS count
                FROM appointment a
                WHERE a.status_appointments = 'COMPLETADA'
                GROUP BY TO_CHAR(a.schedule_date_time, 'YYYY')
                ORDER BY period
            """, nativeQuery = true)
    List<Object[]> findYearlyAppointments();

    @Query(value = """
            SELECT
                e.name || ' ' || e.last_name AS vetName,
                COUNT(*) AS totalAppointments,
                SUM(CASE WHEN a.status_appointments = 'COMPLETADA' THEN 1 ELSE 0 END) AS completed,
                SUM(CASE WHEN a.status_appointments = 'CANCELADA' THEN 1 ELSE 0 END) AS cancelled
            FROM appointment a
            JOIN employee e ON a.employee_id = e.employee_id
            GROUP BY e.name, e.last_name
            ORDER BY totalAppointments DESC
            """, nativeQuery = true)
    List<AppointmentsByVetDTO> findAppointmentsByVet();

    @Query(value = """
            SELECT
                vs.name AS serviceName,
                vs.dir_image AS image,
                COUNT(*) AS count
            FROM appointment a
            JOIN headquarter_vet_service hvs ON a.headquarter_vetservice_id = hvs.id
            JOIN veterinary_service vs ON hvs.id_service = vs.service_id
            WHERE a.status_appointments = 'COMPLETADA'
            GROUP BY vs.name, vs.dir_image
            ORDER BY count DESC
            """, nativeQuery = true)
    List<PopularServicesDTO> findPopularServices();

    @Query(value = """
            SELECT
                s.name AS type,
                b.name AS name,
                s.image_path AS image,
                COUNT(*) AS count
            FROM appointment a
            JOIN animal an ON a.animal_id = an.animal_id
            JOIN breed b ON an.breed_id = b.breed_id
            JOIN specie s ON b.id_specie = s.specie_id
            WHERE a.status_appointments = 'COMPLETADA'
            GROUP BY s.name, b.name, s.image_path
            ORDER BY count DESC
            """, nativeQuery = true)
    List<AnimalsByTypeDTO> findAnimalsBySpecieOrBreed();

    @Query(value = """
                SELECT
                    vetName,
                    period,
                    COUNT(*) AS completedAppointments
                FROM (
                    SELECT
                        (e.name || ' ' || e.last_name) AS vetName,
                        TO_CHAR(a.schedule_date_time, :pattern) AS period
                    FROM appointment a
                    JOIN employee e ON a.employee_id = e.employee_id
                    WHERE a.status_appointments = 'COMPLETADA'
                )
                GROUP BY vetName, period
                ORDER BY period, vetName
            """, nativeQuery = true)
    List<AppointmentsByVetAndPeriodDTO> findAppointmentsByVetAndPeriod(@Param("pattern") String pattern);

}
