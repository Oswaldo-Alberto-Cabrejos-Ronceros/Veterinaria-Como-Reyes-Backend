package com.veterinaria.veterinaria_comoreyes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veterinaria.veterinaria_comoreyes.entity.Care;

public interface CareRepository extends JpaRepository<Care, Long> {

    List<Care> findByAppointment_AppointmentId(Long appointmentId);

}
