package sitema.monitorizacion.Api.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String medicalRecordNumber;
    private List<UserResponse> nurses;
} 