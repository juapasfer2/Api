package sitema.monitorizacion.Api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.AlertResponse;
import sitema.monitorizacion.Api.dto.VitalReadingResponse;
import sitema.monitorizacion.Api.model.Alert;
import sitema.monitorizacion.Api.model.User;
import sitema.monitorizacion.Api.repository.AlertRepository;
import sitema.monitorizacion.Api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final VitalReadingService vitalReadingService;
    
    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }
    
    public List<AlertResponse> getUnacknowledgedAlerts() {
        return alertRepository.findByAcknowledged(false).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }
    
    public List<AlertResponse> getAlertsByNurseId(Long nurseId) {
        return alertRepository.findAlertsByNurseId(nurseId).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }
    
    public List<AlertResponse> getUnacknowledgedAlertsByNurseId(Long nurseId) {
        return alertRepository.findAlertsByNurseIdAndAcknowledged(nurseId, false).stream()
                .map(this::mapToAlertResponse)
                .collect(Collectors.toList());
    }
    
    public AlertResponse getAlertById(Long id) {
        Alert alert = getAlertEntityById(id);
        return mapToAlertResponse(alert);
    }
    
    @Transactional
    public AlertResponse acknowledgeAlert(Long alertId, Long userId) {
        Alert alert = getAlertEntityById(alertId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        
        // Verificar si ya ha sido reconocida
        if (alert.isAcknowledged()) {
            throw new RuntimeException("La alerta ya ha sido reconocida");
        }
        
        // Actualizar la alerta
        alert.setAcknowledged(true);
        alert.setAcknowledgedBy(user);
        Alert updatedAlert = alertRepository.save(alert);
        
        return mapToAlertResponse(updatedAlert);
    }
    
    // Método auxiliar para obtener una entidad Alert por ID
    private Alert getAlertEntityById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con ID: " + id));
    }
    
    // Mapear Alert a AlertResponse
    private AlertResponse mapToAlertResponse(Alert alert) {
        VitalReadingResponse readingResponse = vitalReadingService.getVitalReadingById(alert.getReading().getId());
        
        return AlertResponse.builder()
                .id(alert.getId())
                .reading(readingResponse)
                .level(alert.getLevel())
                .timestamp(alert.getTimestamp())
                .acknowledged(alert.isAcknowledged())
                .acknowledgedBy(alert.getAcknowledgedBy() != null 
                        ? mapUserToUserResponse(alert.getAcknowledgedBy()) 
                        : null)
                .build();
    }
    
    // Método auxiliar para mapear User a UserResponse simple
    private sitema.monitorizacion.Api.dto.UserResponse mapUserToUserResponse(User user) {
        return sitema.monitorizacion.Api.dto.UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
} 