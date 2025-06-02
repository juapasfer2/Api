package sitema.monitorizacion.Api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalTypeRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @NotBlank(message = "La unidad de medida es obligatoria")
    private String unit;
    
    @NotNull(message = "El valor mínimo normal es obligatorio")
    private Double normalMin;
    
    @NotNull(message = "El valor máximo normal es obligatorio")
    private Double normalMax;
} 