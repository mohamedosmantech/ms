package com.musalasoft.drones.controller;

import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import com.musalasoft.drones.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/")
public class DroneController {
    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @Operation(summary = "Register a new drone")
    @PostMapping("register")
    public ResponseEntity<Drone> registerDrone(@Valid @RequestBody DroneDTO droneDTO) {
        log.info("Registering drone {}", droneDTO.getSerialNumber());
        Drone drone = droneService.registerDrone(droneDTO);
        log.info("Registered drone {}", droneDTO.getSerialNumber());
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Load a list of medications into a drone")
    @PostMapping("loadMedications/{droneId}")
    public ResponseEntity<Drone> loadDroneWithMedications(@PathVariable(name = "droneId") long droneId,
                                                          @Valid @RequestBody List<MedicationDTO> medications) {
        log.info("Loading drone {} with {} medications", droneId, medications.size());
        Drone drone = droneService.loadDroneWithMedications(droneId, medications);
        log.info("Loaded drone {} with {} medications", droneId, medications.size());
        return ResponseEntity.ok(drone);
    }

    @Operation(summary = "Fetch all the (current) medications loaded into a drone")
    @GetMapping("medications/{droneId}")
    public ResponseEntity<List<Medication>> getDroneMedications(@PathVariable(name = "droneId") long droneId) {
        log.info("Fetching loaded medications for drone {}", droneId);
        List<Medication> medications = droneService.getDroneMedications(droneId);
        log.info("Fetched loaded medications for drone {}", droneId);
        return ResponseEntity.ok(medications);
    }

    @Operation(summary = "Fetch all drones in IDLE state")
    @GetMapping("available")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        log.info("Checking available drones for loading");
        List<Drone> drones = droneService.getAvailableDrones();
        log.info("Checked available drones for loading");
        return ResponseEntity.ok(drones);
    }

    @Operation(summary = "Get the battery level of a specific drone")
    @GetMapping("battery-level/{droneId}")
    public ResponseEntity<BigDecimal> getDroneBatteryLevel(@PathVariable(name = "droneId") long droneId) {
        log.info("Checking battery level for drone {}", droneId);
        BigDecimal batteryLevel = droneService.getDroneBatteryLevel(droneId);
        log.info("Checked battery level for drone {}", droneId);
        return ResponseEntity.ok(batteryLevel);
    }
}
