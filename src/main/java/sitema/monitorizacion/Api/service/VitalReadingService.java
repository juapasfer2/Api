package sitema.monitorizacion.Api.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.PatientResponse;
import sitema.monitorizacion.Api.dto.VitalReadingRequest;
import sitema.monitorizacion.Api.dto.VitalReadingResponse;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.model.Alert;
import sitema.monitorizacion.Api.model.Patient;
import sitema.monitorizacion.Api.model.Threshold;
import sitema.monitorizacion.Api.model.VitalReading;
import sitema.monitorizacion.Api.model.VitalType;
import sitema.monitorizacion.Api.repository.AlertRepository;
import sitema.monitorizacion.Api.repository.PatientRepository;
import sitema.monitorizacion.Api.repository.ThresholdRepository;
import sitema.monitorizacion.Api.repository.VitalReadingRepository;
import sitema.monitorizacion.Api.repository.VitalTypeRepository;

@Service
@RequiredArgsConstructor
public class VitalReadingService {
    
    private final VitalReadingRepository vitalReadingRepository;
    private final PatientRepository patientRepository;
    private final VitalTypeRepository vitalTypeRepository;
    private final ThresholdRepository thresholdRepository;
    private final AlertRepository alertRepository;
    private final PatientService patientService;
    private final VitalTypeService vitalTypeService;
    
    public List<VitalReadingResponse> getAllVitalReadings() {
        return vitalReadingRepository.findAll().stream()
                .map(this::mapToVitalReadingResponse)
                .collect(Collectors.toList());
    }
    
    public VitalReadingResponse getVitalReadingById(Long id) {
        VitalReading vitalReading = getVitalReadingEntityById(id);
        return mapToVitalReadingResponse(vitalReading);
    }
    
    public List<VitalReadingResponse> getVitalReadingsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + patientId));
        
        return vitalReadingRepository.findByPatient(patient).stream()
                .map(this::mapToVitalReadingResponse)
                .collect(Collectors.toList());
    }
    
    public List<VitalReadingResponse> getVitalReadingsByPatientAndType(Long patientId, Long typeId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + patientId));
        
        VitalType vitalType = vitalTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + typeId));
        
        return vitalReadingRepository.findByPatientAndType(patient, vitalType).stream()
                .map(this::mapToVitalReadingResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public VitalReadingResponse createVitalReading(VitalReadingRequest vitalReadingRequest) {
        Patient patient = patientRepository.findById(vitalReadingRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + vitalReadingRequest.getPatientId()));
        
        VitalType vitalType = vitalTypeRepository.findById(vitalReadingRequest.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + vitalReadingRequest.getTypeId()));
        
        LocalDateTime timestamp = vitalReadingRequest.getTimestamp();
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        
        VitalReading vitalReading = VitalReading.builder()
                .patient(patient)
                .type(vitalType)
                .value(vitalReadingRequest.getValue())
                .timestamp(timestamp)
                .alerts(new ArrayList<>())
                .build();
        
        VitalReading savedVitalReading = vitalReadingRepository.save(vitalReading);
        
        // Verificar si la lectura genera alertas
        checkForAlerts(savedVitalReading);
        
        return mapToVitalReadingResponse(savedVitalReading);
    }
    
    public VitalReadingResponse updateVitalReading(Long id, VitalReadingRequest vitalReadingRequest) {
        VitalReading vitalReading = getVitalReadingEntityById(id);
        
        Patient patient = patientRepository.findById(vitalReadingRequest.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con ID: " + vitalReadingRequest.getPatientId()));
        
        VitalType vitalType = vitalTypeRepository.findById(vitalReadingRequest.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + vitalReadingRequest.getTypeId()));
        
        vitalReading.setPatient(patient);
        vitalReading.setType(vitalType);
        vitalReading.setValue(vitalReadingRequest.getValue());
        
        if (vitalReadingRequest.getTimestamp() != null) {
            vitalReading.setTimestamp(vitalReadingRequest.getTimestamp());
        }
        
        VitalReading updatedVitalReading = vitalReadingRepository.save(vitalReading);
        return mapToVitalReadingResponse(updatedVitalReading);
    }
    
    public void deleteVitalReading(Long id) {
        VitalReading vitalReading = getVitalReadingEntityById(id);
        vitalReadingRepository.delete(vitalReading);
    }
    
    // Método para verificar si una lectura genera alertas
    @Transactional
    public void checkForAlerts(VitalReading vitalReading) {
        // Buscar umbrales para el tipo de constante vital
        Threshold threshold = thresholdRepository.findByTypeId(vitalReading.getType().getId())
                .orElseGet(() -> {
                    // Si no hay un umbral personalizado, usar los valores normales del tipo de constante vital
                    VitalType vitalType = vitalReading.getType();
                    return Threshold.builder()
                            .type(vitalType)
                            .minValue(vitalType.getNormalMin())
                            .maxValue(vitalType.getNormalMax())
                            .build();
                });
        
        double value = vitalReading.getValue();
        String level = null;
        
        // Determinar si el valor está fuera de los umbrales
        if (value < threshold.getMinValue()) {
            level = "BAJO";
        } else if (value > threshold.getMaxValue()) {
            level = "ALTO";
        }
        
        // Si está fuera de los umbrales, crear una alerta
        if (level != null) {
            Alert alert = Alert.builder()
                    .reading(vitalReading)
                    .level(level)
                    .timestamp(LocalDateTime.now())
                    .acknowledged(false)
                    .build();
            
            alertRepository.save(alert);
        }
    }
    
    // Método auxiliar para obtener una entidad VitalReading por ID
    private VitalReading getVitalReadingEntityById(Long id) {
        return vitalReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lectura de constante vital no encontrada con ID: " + id));
    }
    
    // Mapear VitalReading a VitalReadingResponse
    private VitalReadingResponse mapToVitalReadingResponse(VitalReading vitalReading) {
        PatientResponse patientResponse = patientService.getPatientById(vitalReading.getPatient().getId());
        VitalTypeResponse typeResponse = vitalTypeService.getVitalTypeById(vitalReading.getType().getId());
        
        boolean hasAlerts = false;
        if (vitalReading.getAlerts() != null) {
            hasAlerts = !vitalReading.getAlerts().isEmpty();
        }
        
        return VitalReadingResponse.builder()
                .id(vitalReading.getId())
                .patient(patientResponse)
                .type(typeResponse)
                .value(vitalReading.getValue())
                .timestamp(vitalReading.getTimestamp())
                .hasAlerts(hasAlerts)
                .build();
    }
} 