package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Animal.RecentPatientsDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.InfoVeterinaryRecordForTableDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.RecentMedicalRecordDTO;
import com.veterinaria.veterinaria_comoreyes.dto.VeterinaryRecord.VeterinaryRecordStatsDTO;
import com.veterinaria.veterinaria_comoreyes.service.IAnimalService;
import com.veterinaria.veterinaria_comoreyes.service.ICareService;
import com.veterinaria.veterinaria_comoreyes.service.IVeterinaryRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/panel/veterinarians")
public class VeterinarianController {

    private final IVeterinaryRecordService veterinaryRecordService;
    private final ICareService careService;
    private final IAnimalService animalService;

    public VeterinarianController(IVeterinaryRecordService veterinaryRecordService, ICareService careService, IAnimalService animalService) {
        this.veterinaryRecordService = veterinaryRecordService;
        this.careService = careService;
        this.animalService = animalService;
    }

    //Recent Medical Record for Veterinarian Panel
    @GetMapping("/recent-medical-records/{employeeId}")
    public ResponseEntity<List<RecentMedicalRecordDTO>> getRecentRecordsByEmployee(@PathVariable Long employeeId) {
        List<RecentMedicalRecordDTO> records = veterinaryRecordService.getRecentRecordsByEmployee(employeeId);
        return ResponseEntity.ok(records);
    }

    // Statistics Medical Record for Veterinarian Panel
    @GetMapping("/medical-records/statistics/{employeeId}")
    public ResponseEntity<VeterinaryRecordStatsDTO> getStatsByVeterinarian(@PathVariable Long employeeId) {
        VeterinaryRecordStatsDTO stats = veterinaryRecordService.getVeterinaryRecordStatsByEmployee(employeeId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/care/recent-patients/{employeeId}")
    public ResponseEntity<List<RecentPatientsDTO>> getRecentPatients(@PathVariable Long employeeId) {
        return ResponseEntity.ok(careService.getRecentPatients(employeeId));
    }

    @GetMapping("/veterinary-records/animal/{animalId}")
    public ResponseEntity<List<InfoVeterinaryRecordForTableDTO>> getRecordsByAnimalId(@PathVariable Long animalId) {
        List<InfoVeterinaryRecordForTableDTO> records = veterinaryRecordService.getRecordsByAnimalId(animalId);
        return ResponseEntity.ok(records);
    }

}
