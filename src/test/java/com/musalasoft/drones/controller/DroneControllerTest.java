package com.musalasoft.drones.controller;

import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.service.DroneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DroneControllerTest {
    @InjectMocks
    DroneController toTest;
    @Mock
    DroneService droneService;

    @Test
    void registerDrone_withExistingSerialNumber() {
        when(droneService.registerDrone(new DroneDTO())).thenThrow(IllegalStateException.class);
        assertThrows(IllegalStateException.class, () -> toTest.registerDrone(new DroneDTO()));
    }

    @Test
    void registerDrone() {
        when(droneService.registerDrone(new DroneDTO())).thenReturn(new Drone());
        ResponseEntity<Drone> response = toTest.registerDrone(new DroneDTO());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void loadDroneWithMedicationsThrowsIllegalStateException() {
        when(droneService.loadDroneWithMedications(anyLong(), any())).thenThrow(IllegalStateException.class);
        assertThrows(IllegalStateException.class, () -> toTest.loadDroneWithMedications(1, List.of(new MedicationDTO())));
    }

    @Test
    void loadDroneWithMedicationsThrowsIllegalArgumentException() {
        when(droneService.loadDroneWithMedications(anyLong(), any())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> toTest.loadDroneWithMedications(1, List.of(new MedicationDTO())));
    }

    @Test
    void loadDroneWithMedications() {
        when(droneService.loadDroneWithMedications(anyLong(), any())).thenReturn(new Drone());

        ResponseEntity<Drone> response = toTest.loadDroneWithMedications(1, List.of(new MedicationDTO()));
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
