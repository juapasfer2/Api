package sitema.monitorizacion.Api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThresholdRequest {
    
    @NotNull(message = "El tipo de constante vital es obligatorio")
    private Long typeId;
    
    @NotNull(message = "El valor mínimo es obligatorio")
    private Double minValue;
    
    @NotNull(message = "El valor máximo es obligatorio")
    private Double maxValue;
} 