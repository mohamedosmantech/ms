package com.musalasoft.drones.service;


import com.musalasoft.drones.constants.DroneConstant;
import com.musalasoft.drones.mapper.DroneMapperImpl;
import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import com.musalasoft.drones.model.enums.Model;
import com.musalasoft.drones.model.enums.State;
import com.musalasoft.drones.repository.DroneRepository;
import com.musalasoft.drones.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        DroneService.class,
        DroneMapperImpl.class
})
public class DroneServiceTest {
    @Autowired
    private DroneService toTest;
    @MockBean
    private DroneRepository droneRepository;
    @MockBean
    private MedicationRepository medicationRepository;
    @MockBean
    private DroneConstant droneConstant;
    @Autowired
    DroneMapperImpl droneMapper;

    private DroneDTO droneDTO;
    private Drone drone;
    private MedicationDTO medicationDTO;
    private Medication medication;
    private static final long DRONE_ID = 1;

    @BeforeEach
    void setUp() {
        droneDTO = DroneDTO.builder()
                .serialNumber("12wer")
                .batteryCapacity(new BigDecimal(60))
                .model(Model.MIDDLEWEIGHT)
                .weight(new BigDecimal(200))
                .build();

        medicationDTO = MedicationDTO.builder()
                .code("CDE")
                .name("Medication")
                .weight(new BigDecimal(50))
                .image("base64(dfgvbu6i8")
                .build();

        drone = droneMapper.droneDTOToDroneEntity(droneDTO);
        drone.setId(DRONE_ID);
        drone.setState(State.IDLE);

        medication = droneMapper.medicationDTOToMedicationEntity(medicationDTO);
        medication.setDrone(drone);
    }

    @Test
    void registerDrone_withExistingSerialNumber() {
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.ofNullable(drone));

        assertThrows(IllegalStateException.class, () -> toTest.registerDrone(droneDTO));
    }

    @Test
    void registerDrone() {
        when(droneRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());
        when(droneRepository.save(any())).thenReturn(drone);
        Drone drone = toTest.registerDrone(droneDTO);

        assertNotNull(drone);
        System.out.println(drone.getId());
        assertNotEquals(0, drone.getId());
        assertEquals(State.IDLE, drone.getState());
    }

    @Test
    void loadDroneWithMedications_invalidID() {
        when(droneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> toTest.loadDroneWithMedications(DRONE_ID, List.of(medicationDTO)));

    }

    @Test
    void loadDroneWithMedications_lowBattery() {
        drone.setBatteryCapacity(new BigDecimal(23));
        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(droneConstant.getBatteryCapacityLowLevel()).thenReturn(25);
        assertThrows(IllegalStateException.class, () -> toTest.loadDroneWithMedications(DRONE_ID, List.of(medicationDTO)));

    }

    @Test
    void loadDroneWithMedications_overloaded() {
        medication.setWeight(new BigDecimal(500));
        drone.setActiveLoadingSession("active");
        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByDroneAndLoadingSession(any(), anyString())).thenReturn((List.of(medication)));

        assertThrows(IllegalArgumentException.class, () -> toTest.loadDroneWithMedications(DRONE_ID, List.of(medicationDTO)));

    }

    @Test
    void loadDroneWithMedications_notAvailable() {
        drone.setState(State.DELIVERING);
        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByDroneAndLoadingSession(any(), anyString())).thenReturn((List.of(medication)));

        assertThrows(IllegalStateException.class, () -> toTest.loadDroneWithMedications(DRONE_ID, List.of(medicationDTO)));

    }

    @Test
    void loadDroneWithMedications() {
        when(droneRepository.findById(any())).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllByDroneAndLoadingSession(any(), anyString())).thenReturn((List.of(medication)));

        drone = toTest.loadDroneWithMedications(DRONE_ID, List.of(medicationDTO));

        assertNotNull(drone);
        assertNotNull(drone.getActiveLoadingSession());

        verify(droneRepository, times(1)).save(any());
        ArgumentCaptor<List<Medication>> medicationCaptor = ArgumentCaptor.forClass(List.class);
        verify(medicationRepository, times(1)).saveAll(medicationCaptor.capture());

        assertEquals(1, medicationCaptor.getValue().size());
        assertEquals(DRONE_ID, medicationCaptor.getValue().get(0).getDrone().getId());
        assertNotNull(medicationCaptor.getValue().get(0).getLoadingSession());

    }

}
