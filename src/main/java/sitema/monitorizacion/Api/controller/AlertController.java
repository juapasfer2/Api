package sitema.monitorizacion.Api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.AlertAcknowledgeRequest;
import sitema.monitorizacion.Api.dto.AlertResponse;
import sitema.monitorizacion.Api.service.AlertService;
import sitema.monitorizacion.Api.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AlertController {
    
    private final AlertService alertService;
    private final UserService userService;
    
    // ========== ENDPOINTS PARA ADMINISTRADORES ==========
    
    @GetMapping("/admin/alerts")
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
    
    @GetMapping("/admin/alerts/unacknowledged")
    public ResponseEntity<List<AlertResponse>> getAllUnacknowledgedAlerts() {
        return ResponseEntity.ok(alertService.getUnacknowledgedAlerts());
    }
    
    @GetMapping("/admin/alerts/{id}")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.getAlertById(id));
    }
    
    @PostMapping("/admin/alerts/{id}/acknowledge")
    public ResponseEntity<AlertResponse> acknowledgeAlertAsAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AlertAcknowledgeRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        Long userId = userService.getUserByEmail(email).getId();
        return ResponseEntity.ok(alertService.acknowledgeAlert(id, userId));
    }
    
    // ========== ENDPOINTS PARA ENFERMERAS ==========
    
    @PostMapping("/nurse/alerts/{id}/acknowledge")
    public ResponseEntity<AlertResponse> acknowledgeAlertAsNurse(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        
        // Verificar que la alerta pertenezca a un paciente asignado a esta enfermera
        AlertResponse alert = alertService.getAlertById(id);
        List<AlertResponse> nurseAlerts = alertService.getAlertsByNurseId(nurseId);
        
        boolean isMyAlert = nurseAlerts.stream()
                .anyMatch(a -> a.getId().equals(id));
        
        if (!isMyAlert) {
            throw new RuntimeException("No tienes permiso para reconocer esta alerta");
        }
        
        return ResponseEntity.ok(alertService.acknowledgeAlert(id, nurseId));
    }
    
    @GetMapping("/nurse/alerts/{id}")
    public ResponseEntity<AlertResponse> getMyAlertById(
            @PathVariable Long id, 
            Authentication authentication) {
        String email = authentication.getName();
        Long nurseId = userService.getUserByEmail(email).getId();
        
        // Verificar que la alerta pertenezca a un paciente asignado a esta enfermera
        List<AlertResponse> nurseAlerts = alertService.getAlertsByNurseId(nurseId);
        boolean isMyAlert = nurseAlerts.stream()
                .anyMatch(a -> a.getId().equals(id));
        
        if (!isMyAlert) {
            throw new RuntimeException("No tienes permiso para acceder a esta alerta");
        }
        
        return ResponseEntity.ok(alertService.getAlertById(id));
    }
    
    // ========== ENDPOINTS COMUNES ==========
    
    @GetMapping({"/admin/alerts/nurse/{nurseId}", "/nurse/alerts/nurse/{nurseId}"})
    public ResponseEntity<List<AlertResponse>> getAlertsByNurse(
            @PathVariable Long nurseId,
            Authentication authentication) {
        
        // Si es una enfermera, solo puede ver sus propias alertas
        String email = authentication.getName();
        Long currentUserId = userService.getUserByEmail(email).getId();
        String userRole = userService.getUserByEmail(email).getRole().getName();
        
        if ("NURSE".equals(userRole) && !currentUserId.equals(nurseId)) {
            throw new RuntimeException("No tienes permiso para ver las alertas de otra enfermera");
        }
        
        return ResponseEntity.ok(alertService.getAlertsByNurseId(nurseId));
    }
    
    @GetMapping({"/admin/alerts/nurse/{nurseId}/unacknowledged", "/nurse/alerts/nurse/{nurseId}/unacknowledged"})
    public ResponseEntity<List<AlertResponse>> getUnacknowledgedAlertsByNurse(
            @PathVariable Long nurseId,
            Authentication authentication) {
        
        // Si es una enfermera, solo puede ver sus propias alertas
        String email = authentication.getName();
        Long currentUserId = userService.getUserByEmail(email).getId();
        String userRole = userService.getUserByEmail(email).getRole().getName();
        
        if ("NURSE".equals(userRole) && !currentUserId.equals(nurseId)) {
            throw new RuntimeException("No tienes permiso para ver las alertas de otra enfermera");
        }
        
        return ResponseEntity.ok(alertService.getUnacknowledgedAlertsByNurseId(nurseId));
    }
} 