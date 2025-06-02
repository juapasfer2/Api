package sitema.monitorizacion.Api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.ThresholdRequest;
import sitema.monitorizacion.Api.dto.ThresholdResponse;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.model.Threshold;
import sitema.monitorizacion.Api.model.VitalType;
import sitema.monitorizacion.Api.repository.ThresholdRepository;
import sitema.monitorizacion.Api.repository.VitalTypeRepository;

@Service
@RequiredArgsConstructor
public class ThresholdService {
    
    private final ThresholdRepository thresholdRepository;
    private final VitalTypeRepository vitalTypeRepository;
    private final VitalTypeService vitalTypeService;
    
    public List<ThresholdResponse> getAllThresholds() {
        return thresholdRepository.findAll().stream()
                .map(this::mapToThresholdResponse)
                .collect(Collectors.toList());
    }
    
    public ThresholdResponse getThresholdById(Long id) {
        Threshold threshold = getThresholdEntityById(id);
        return mapToThresholdResponse(threshold);
    }
    
    public ThresholdResponse getThresholdByTypeId(Long typeId) {
        Threshold threshold = thresholdRepository.findByTypeId(typeId)
                .orElseThrow(() -> new RuntimeException("Umbral no encontrado para el tipo de constante vital con ID: " + typeId));
        return mapToThresholdResponse(threshold);
    }
    
    public List<ThresholdResponse> getThresholdsByType(Long typeId) {
        VitalType vitalType = vitalTypeRepository.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + typeId));
        
        return thresholdRepository.findByType(vitalType).stream()
                .map(this::mapToThresholdResponse)
                .collect(Collectors.toList());
    }
    
    public ThresholdResponse createThreshold(ThresholdRequest thresholdRequest) {
        VitalType vitalType = vitalTypeRepository.findById(thresholdRequest.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + thresholdRequest.getTypeId()));
        
        Threshold threshold = Threshold.builder()
                .type(vitalType)
                .minValue(thresholdRequest.getMinValue())
                .maxValue(thresholdRequest.getMaxValue())
                .build();
        
        Threshold savedThreshold = thresholdRepository.save(threshold);
        return mapToThresholdResponse(savedThreshold);
    }
    
    public ThresholdResponse updateThreshold(Long id, ThresholdRequest thresholdRequest) {
        Threshold threshold = getThresholdEntityById(id);
        
        if (!threshold.getType().getId().equals(thresholdRequest.getTypeId())) {
            VitalType vitalType = vitalTypeRepository.findById(thresholdRequest.getTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + thresholdRequest.getTypeId()));
            threshold.setType(vitalType);
        }
        
        threshold.setMinValue(thresholdRequest.getMinValue());
        threshold.setMaxValue(thresholdRequest.getMaxValue());
        
        Threshold updatedThreshold = thresholdRepository.save(threshold);
        return mapToThresholdResponse(updatedThreshold);
    }
    
    public void deleteThreshold(Long id) {
        Threshold threshold = getThresholdEntityById(id);
        thresholdRepository.delete(threshold);
    }
    
    // Método auxiliar para obtener una entidad Threshold por ID
    private Threshold getThresholdEntityById(Long id) {
        return thresholdRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Umbral no encontrado con ID: " + id));
    }
    
    // Mapear Threshold a ThresholdResponse
    private ThresholdResponse mapToThresholdResponse(Threshold threshold) {
        VitalTypeResponse vitalTypeResponse = vitalTypeService.getVitalTypeById(threshold.getType().getId());
        
        return ThresholdResponse.builder()
                .id(threshold.getId())
                .type(vitalTypeResponse)
                .minValue(threshold.getMinValue())
                .maxValue(threshold.getMaxValue())
                .build();
    }
} 