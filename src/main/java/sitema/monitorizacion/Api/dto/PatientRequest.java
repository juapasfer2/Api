package sitema.monitorizacion.Api.dto;

import java.time.LocalDate;
import java.util.List;

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
public class PatientRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "El número de historial médico es obligatorio")
    private String medicalRecordNumber;
    
    private List<Long> nurseIds;
} 