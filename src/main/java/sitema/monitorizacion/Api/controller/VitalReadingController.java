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
import sitema.monitorizacion.Api.dto.VitalReadingRequest;
import sitema.monitorizacion.Api.dto.VitalReadingResponse;
import sitema.monitorizacion.Api.service.VitalReadingService;

@RestController
@RequestMapping({"/api/admin/vital-readings", "/api/nurse/vital-readings"})
@RequiredArgsConstructor
public class VitalReadingController {
    
    private final VitalReadingService vitalReadingService;
    
    @GetMapping
    public ResponseEntity<List<VitalReadingResponse>> getAllVitalReadings() {
        return ResponseEntity.ok(vitalReadingService.getAllVitalReadings());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VitalReadingResponse> getVitalReadingById(@PathVariable Long id) {
        return ResponseEntity.ok(vitalReadingService.getVitalReadingById(id));
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VitalReadingResponse>> getVitalReadingsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(vitalReadingService.getVitalReadingsByPatient(patientId));
    }
    
    @GetMapping("/patient/{patientId}/type/{typeId}")
    public ResponseEntity<List<VitalReadingResponse>> getVitalReadingsByPatientAndType(
            @PathVariable Long patientId, @PathVariable Long typeId) {
        return ResponseEntity.ok(vitalReadingService.getVitalReadingsByPatientAndType(patientId, typeId));
    }
    
    @PostMapping
    public ResponseEntity<VitalReadingResponse> createVitalReading(@Valid @RequestBody VitalReadingRequest vitalReadingRequest) {
        return new ResponseEntity<>(vitalReadingService.createVitalReading(vitalReadingRequest), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VitalReadingResponse> updateVitalReading(@PathVariable Long id, @Valid @RequestBody VitalReadingRequest vitalReadingRequest) {
        return ResponseEntity.ok(vitalReadingService.updateVitalReading(id, vitalReadingRequest));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitalReading(@PathVariable Long id) {
        vitalReadingService.deleteVitalReading(id);
        return ResponseEntity.noContent().build();
    }
} 