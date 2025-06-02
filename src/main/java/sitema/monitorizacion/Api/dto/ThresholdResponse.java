package sitema.monitorizacion.Api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThresholdResponse {
    
    private Long id;
    private VitalTypeResponse type;
    private Double minValue;
    private Double maxValue;
} 