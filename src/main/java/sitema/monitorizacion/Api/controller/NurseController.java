package sitema.monitorizacion.Api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.AlertResponse;
import sitema.monitorizacion.Api.dto.PatientResponse;
import sitema.monitorizacion.Api.dto.VitalReadingResponse;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.service.AlertService;
import sitema.monitorizacion.Api.service.PatientService;
import sitema.monitorizacion.Api.service.UserService;
import sitema.monitorizacion.Api.service.VitalReadingService;
import sitema.monitorizacion.Api.service.VitalTypeService;

@RestController
@RequestMapping("/api/nurse")
@RequiredArgsConstructor
public class NurseController {
    
    private final PatientService patientService;
    private final VitalReadingService vitalReadingService;
    private final AlertService alertService;
    private final UserService userService;
    private final VitalTypeService vitalTypeService;
    
    // Obtener todos los pacientes asignados a la enfermera autenticada
    @GetMapping("/my-patients")
    public ResponseEntity<List<PatientResponse>> getMyPatients(Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(patientService.getPatientsByNurseId(nurseId));
    }
    
    // Obtener un paciente específico asignado a la enfermera
    @GetMapping("/my-patients/{patientId}")
    public ResponseEntity<PatientResponse> getMyPatient(
            @PathVariable Long patientId, 
            Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        
        // Verificar que el paciente esté asignado a esta enfermera
        List<PatientResponse> myPatients = patientService.getPatientsByNurseId(nurseId);
        boolean isMyPatient = myPatients.stream()
                .anyMatch(patient -> patient.getId().equals(patientId));
        
        if (!isMyPatient) {
            throw new RuntimeException("No tienes permiso para acceder a este paciente");
        }
        
        return ResponseEntity.ok(patientService.getPatientById(patientId));
    }
    
    // Obtener todas las constantes vitales de un paciente asignado
    @GetMapping("/my-patients/{patientId}/vital-readings")
    public ResponseEntity<List<VitalReadingResponse>> getPatientVitalReadings(
            @PathVariable Long patientId,
            Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        
        // Verificar que el paciente esté asignado a esta enfermera
        List<PatientResponse> myPatients = patientService.getPatientsByNurseId(nurseId);
        boolean isMyPatient = myPatients.stream()
                .anyMatch(patient -> patient.getId().equals(patientId));
        
        if (!isMyPatient) {
            throw new RuntimeException("No tienes permiso para acceder a este paciente");
        }
        
        return ResponseEntity.ok(vitalReadingService.getVitalReadingsByPatient(patientId));
    }
    
    // Obtener constantes vitales de un tipo específico para un paciente
    @GetMapping("/my-patients/{patientId}/vital-readings/type/{typeId}")
    public ResponseEntity<List<VitalReadingResponse>> getPatientVitalReadingsByType(
            @PathVariable Long patientId,
            @PathVariable Long typeId,
            Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        
        // Verificar que el paciente esté asignado a esta enfermera
        List<PatientResponse> myPatients = patientService.getPatientsByNurseId(nurseId);
        boolean isMyPatient = myPatients.stream()
                .anyMatch(patient -> patient.getId().equals(patientId));
        
        if (!isMyPatient) {
            throw new RuntimeException("No tienes permiso para acceder a este paciente");
        }
        
        return ResponseEntity.ok(vitalReadingService.getVitalReadingsByPatientAndType(patientId, typeId));
    }
    
    // Obtener todas las alertas de los pacientes asignados a la enfermera
    @GetMapping("/my-alerts")
    public ResponseEntity<List<AlertResponse>> getMyAlerts(Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(alertService.getAlertsByNurseId(nurseId));
    }
    
    // Obtener solo las alertas no reconocidas de los pacientes asignados
    @GetMapping("/my-alerts/unacknowledged")
    public ResponseEntity<List<AlertResponse>> getMyUnacknowledgedAlerts(Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(alertService.getUnacknowledgedAlertsByNurseId(nurseId));
    }
    
    // Obtener todos los tipos de constantes vitales disponibles
    @GetMapping("/vital-types")
    public ResponseEntity<List<VitalTypeResponse>> getAllVitalTypes() {
        return ResponseEntity.ok(vitalTypeService.getAllVitalTypes());
    }
} 