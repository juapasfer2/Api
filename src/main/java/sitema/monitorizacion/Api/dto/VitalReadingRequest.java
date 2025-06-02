package sitema.monitorizacion.Api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalReadingRequest {
    
    @NotNull(message = "El paciente es obligatorio")
    private Long patientId;
    
    @NotNull(message = "El tipo de constante vital es obligatorio")
    private Long typeId;
    
    @NotNull(message = "El valor es obligatorio")
    private Double value;
    
    private LocalDateTime timestamp;
} 