package sitema.monitorizacion.Api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalTypeResponse {
    
    private Long id;
    private String name;
    private String unit;
    private Double normalMin;
    private Double normalMax;
} 