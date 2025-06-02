package sitema.monitorizacion.Api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerts")
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reading_id", nullable = false)
    private VitalReading reading;
    
    @NotBlank
    @Column(nullable = false)
    private String level;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private boolean acknowledged;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acknowledged_by")
    private User acknowledgedBy;
} 