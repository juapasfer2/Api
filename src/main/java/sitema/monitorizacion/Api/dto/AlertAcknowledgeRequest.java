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
public class AlertAcknowledgeRequest {
    
    @NotNull(message = "El ID de alerta es obligatorio")
    private Long alertId;
} 