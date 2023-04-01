package com.musalasoft.drones.mapper;

import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import com.musalasoft.drones.model.enums.Model;
import com.musalasoft.drones.model.mapper.DroneMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DroneMapperTest {
    @Autowired
    private DroneMapperImpl droneMapper;

    @Test
    void droneDTOToDroneEntity() {
        DroneDTO input = DroneDTO.builder()
                .serialNumber("SER_NO1")
                .model(Model.MIDDLEWEIGHT)
                .weight(new BigDecimal(200))
                .batteryCapacity(new BigDecimal(80))
                .build();
        Drone drone = droneMapper.droneDTOToDroneEntity(input);

        assertEquals(input.getSerialNumber(), drone.getSerialNumber());
        assertEquals(input.getModel(), drone.getModel());
        assertEquals(input.getWeight(), drone.getWeight());
        assertEquals(input.getBatteryCapacity(), drone.getBatteryCapacity());
    }

    @Test
    void medicationDTOtoMedicationEntity() {
        MedicationDTO input = MedicationDTO.builder()
                .code("AEWR")
                .weight(new BigDecimal(45))
                .name("New Med")
                .image("base64(dfghjkl)")
                .build();
        Medication medication = droneMapper.medicationDTOToMedicationEntity(input);

        assertEquals(input.getCode(), medication.getCode());
        assertEquals(input.getName(), medication.getName());
        assertEquals(input.getWeight(), medication.getWeight());
        assertEquals(input.getImage(), medication.getImage());
    }
}
