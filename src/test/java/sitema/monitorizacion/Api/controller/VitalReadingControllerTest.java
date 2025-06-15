package sitema.monitorizacion.Api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import sitema.monitorizacion.Api.dto.PatientResponse;
import sitema.monitorizacion.Api.dto.VitalReadingRequest;
import sitema.monitorizacion.Api.dto.VitalReadingResponse;
import sitema.monitorizacion.Api.dto.VitalTypeResponse;
import sitema.monitorizacion.Api.service.VitalReadingService;

/**
 * Tests unitarios simples para VitalReadingController
 * Casos de uso: Admin y Enfermera
 */
@ExtendWith(MockitoExtension.class)
class VitalReadingControllerTest {

    @Mock
    private VitalReadingService vitalReadingService;

    @InjectMocks
    private VitalReadingController vitalReadingController;

    private VitalReadingResponse vitalReadingResponse;
    private VitalReadingRequest vitalReadingRequest;

    @BeforeEach
    void setUp() {
        // Datos de prueba
        PatientResponse patient = PatientResponse.builder()
                .id(1L)
                .firstName("María")
                .lastName("García")
                .dateOfBirth(LocalDate.of(1985, 3, 20))
                .medicalRecordNumber("PAT001")
                .build();

        VitalTypeResponse vitalType = VitalTypeResponse.builder()
                .id(1L)
                .name("Temperatura Corporal")
                .unit("°C")
                .normalMin(36.0)
                .normalMax(37.5)
                .build();

        vitalReadingResponse = VitalReadingResponse.builder()
                .id(1L)
                .patient(patient)
                .type(vitalType)
                .value(36.8)
                .timestamp(LocalDateTime.now())
                .hasAlerts(false)
                .build();

        vitalReadingRequest = VitalReadingRequest.builder()
                .patientId(1L)
                .typeId(1L)
                .value(36.8)
                .build();
    }

    @Test
    @DisplayName("Admin - Debería obtener todas las lecturas vitales")
    void adminShouldGetAllVitalReadings() {
        // Given
        List<VitalReadingResponse> readings = Arrays.asList(vitalReadingResponse);
        when(vitalReadingService.getAllVitalReadings()).thenReturn(readings);

        // When
        ResponseEntity<List<VitalReadingResponse>> response = vitalReadingController.getAllVitalReadings();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("María", response.getBody().get(0).getPatient().getFirstName());
        assertEquals(36.8, response.getBody().get(0).getValue());
    }

    @Test
    @DisplayName("Enfermera - Debería crear una nueva lectura vital")
    void nurseShouldCreateVitalReading() {
        // Given
        when(vitalReadingService.createVitalReading(any(VitalReadingRequest.class)))
                .thenReturn(vitalReadingResponse);

        // When
        ResponseEntity<VitalReadingResponse> response = vitalReadingController.createVitalReading(vitalReadingRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(36.8, response.getBody().getValue());
        assertEquals("Temperatura Corporal", response.getBody().getType().getName());
    }
} 