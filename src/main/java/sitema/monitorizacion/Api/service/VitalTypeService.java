package sitema.monitorizacion.Api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sitema.monitorizacion.Api.dto.VitalTypeRequest;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.model.VitalType;
import sitema.monitorizacion.Api.repository.VitalTypeRepository;

@Service
@RequiredArgsConstructor
public class VitalTypeService {
    
    private final VitalTypeRepository vitalTypeRepository;
    
    public List<VitalTypeResponse> getAllVitalTypes() {
        return vitalTypeRepository.findAll().stream()
                .map(this::mapToVitalTypeResponse)
                .collect(Collectors.toList());
    }
    
    public VitalTypeResponse getVitalTypeById(Long id) {
        VitalType vitalType = getVitalTypeEntityById(id);
        return mapToVitalTypeResponse(vitalType);
    }
    
    public VitalTypeResponse createVitalType(VitalTypeRequest vitalTypeRequest) {
        VitalType vitalType = VitalType.builder()
                .name(vitalTypeRequest.getName())
                .unit(vitalTypeRequest.getUnit())
                .normalMin(vitalTypeRequest.getNormalMin())
                .normalMax(vitalTypeRequest.getNormalMax())
                .build();
        
        VitalType savedVitalType = vitalTypeRepository.save(vitalType);
        return mapToVitalTypeResponse(savedVitalType);
    }
    
    public VitalTypeResponse updateVitalType(Long id, VitalTypeRequest vitalTypeRequest) {
        VitalType vitalType = getVitalTypeEntityById(id);
        
        vitalType.setName(vitalTypeRequest.getName());
        vitalType.setUnit(vitalTypeRequest.getUnit());
        vitalType.setNormalMin(vitalTypeRequest.getNormalMin());
        vitalType.setNormalMax(vitalTypeRequest.getNormalMax());
        
        VitalType updatedVitalType = vitalTypeRepository.save(vitalType);
        return mapToVitalTypeResponse(updatedVitalType);
    }
    
    public void deleteVitalType(Long id) {
        VitalType vitalType = getVitalTypeEntityById(id);
        vitalTypeRepository.delete(vitalType);
    }
    
    // Método auxiliar para obtener una entidad VitalType por ID
    private VitalType getVitalTypeEntityById(Long id) {
        return vitalTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de constante vital no encontrado con ID: " + id));
    }
    
    // Mapear VitalType a VitalTypeResponse
    private VitalTypeResponse mapToVitalTypeResponse(VitalType vitalType) {
        return VitalTypeResponse.builder()
                .id(vitalType.getId())
                .name(vitalType.getName())
                .unit(vitalType.getUnit())
                .normalMin(vitalType.getNormalMin())
                .normalMax(vitalType.getNormalMax())
                .build();
    }
} 