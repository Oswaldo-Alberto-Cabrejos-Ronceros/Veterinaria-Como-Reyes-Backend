package com.veterinaria.veterinaria_comoreyes.repository;

import com.veterinaria.veterinaria_comoreyes.entity.Appointment;
import com.veterinaria.veterinaria_comoreyes.entity.Animal;
import com.veterinaria.veterinaria_comoreyes.entity.Employee;
import com.veterinaria.veterinaria_comoreyes.entity.StatusAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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
}
