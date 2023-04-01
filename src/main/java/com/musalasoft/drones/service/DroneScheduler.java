package com.musalasoft.drones.service;


import com.musalasoft.drones.repository.DroneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Slf4j
@Configuration
public class DroneScheduler {
    private final DroneRepository droneRepository;
    private static final String BATTERY_LEVEL_CHECK = "BATTERY_LEVEL_CHECK";

    @Autowired
    public DroneScheduler(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Scheduled(cron = "${drone.battery-check-period}")
    void batteryLevelChecks() {

        //todo change drone state if it's low battery
        log.info("{}: start time: {} ", BATTERY_LEVEL_CHECK, Instant.now());
        droneRepository.findAll().forEach(drone ->
                log.info("<><><> Drone: {}, Battery level: {}", drone.getSerialNumber(), drone.getBatteryCapacity())
        );
        log.info("{}: end time: {} ", BATTERY_LEVEL_CHECK, Instant.now());
    }


}
