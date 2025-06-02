package sitema.monitorizacion.Api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.PatientRequest;
import sitema.monitorizacion.Api.dto.PatientResponse;
import sitema.monitorizacion.Api.service.PatientService;

@RestController
@RequestMapping("/api/admin/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
    
    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        return new ResponseEntity<>(patientService.createPatient(patientRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{patientId}/nurses/{nurseId}")
    public ResponseEntity<PatientResponse> assignNurseToPatient(@PathVariable Long patientId, @PathVariable Long nurseId) {
        return ResponseEntity.ok(patientService.assignNurseToPatient(patientId, nurseId));
    }
    
    @DeleteMapping("/{patientId}/nurses/{nurseId}")
    public ResponseEntity<PatientResponse> removeNurseFromPatient(@PathVariable Long patientId, @PathVariable Long nurseId) {
        return ResponseEntity.ok(patientService.removeNurseFromPatient(patientId, nurseId));
    }
} 