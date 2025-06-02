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
public class AlertResponse {
    
    private Long id;
    private VitalReadingResponse reading;
    private String level;
    private LocalDateTime timestamp;
    private boolean acknowledged;
    private UserResponse acknowledgedBy;
} 