package sitema.monitorizacion.Api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalReadingResponse {
    
    private Long id;
    private PatientResponse patient;
    private VitalTypeResponse type;
    private Double value;
    private LocalDateTime timestamp;
    private boolean hasAlerts;
} 