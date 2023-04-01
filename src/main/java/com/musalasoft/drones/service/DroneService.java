package com.musalasoft.drones.service;


import com.musalasoft.drones.constants.DroneConstant;
import com.musalasoft.drones.model.mapper.DroneMapper;
import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import com.musalasoft.drones.model.enums.State;
import com.musalasoft.drones.repository.DroneRepository;
import com.musalasoft.drones.repository.MedicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DroneService {
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    private final DroneConstant droneConstant;

    @Autowired
    public DroneService(DroneRepository droneRepository, MedicationRepository medicationRepository, DroneMapper droneMapper, DroneConstant droneConstant) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.droneMapper = droneMapper;
        this.droneConstant = droneConstant;
    }

    /**
     * @param droneDTO object with the properties of the drone to be registered
     * @return Drone - newly registered drone
     */
    public Drone registerDrone(DroneDTO droneDTO) {
        droneRepository.findBySerialNumber(droneDTO.getSerialNumber()).ifPresent(drone -> {
            throw new IllegalStateException(String.format("Drone with serial number %s already exists", drone.getSerialNumber()));
        });
        Drone newDrone = droneMapper.droneDTOToDroneEntity(droneDTO);
        newDrone.setState(State.IDLE);
        return droneRepository.save(newDrone);
    }

    /**
     * @param droneId                unique identifier of the drone to be loaded
     * @param medications list of medications to be loaded into the drone
     * @return Drone
     * @throws java.util.NoSuchElementException if a drone with the id does not exist
     */
    public Drone loadDroneWithMedications(long droneId, List<MedicationDTO> medications) {
        Drone drone = droneRepository.findById(droneId).orElseThrow();
        if (drone.getBatteryCapacity().floatValue() < droneConstant.getBatteryCapacityLowLevel()) {
            throw new IllegalStateException(String.format("The drone battery capacity is below %d%%", droneConstant.getBatteryCapacityLowLevel()));
        }
        if (!drone.getState().equals(State.IDLE) && !drone.getState().equals(State.LOADING)) {
            throw new IllegalStateException("The drone can not be loaded at this state");
        }
        if (drone.getState().equals(State.IDLE)) {
            drone.setState(State.LOADING);
            drone.setActiveLoadingSession(UUID.randomUUID().toString());
            droneRepository.save(drone);

            log.info("Updated drone state to {}", State.LOADING);
        }

        List<Medication> droneMedications = medicationRepository.findAllByDroneAndLoadingSession(drone, drone.getActiveLoadingSession());
        validateWight(drone, droneMedications, medications);

        List<Medication> newMedications = medications.stream().map(input -> {
            Medication medication = droneMapper.medicationDTOToMedicationEntity(input);
            medication.setDrone(drone);
            medication.setLoadingSession(drone.getActiveLoadingSession());
            return medication;
        }).collect(Collectors.toList());

        medicationRepository.saveAll(newMedications);

        return drone;
    }

    /**
     * @param droneId unique identifier of the drone to fetch it's loaded medication
     * @return list of medications in the drone
     * @throws java.util.NoSuchElementException if a drone with the id does not exist
     */
    public List<Medication> getDroneMedications(long droneId) {
        Drone drone = droneRepository.findById(droneId).orElseThrow();
        return medicationRepository.findAllByDroneAndLoadingSession(drone, drone.getActiveLoadingSession());
    }

    private void validateWight(Drone drone, List<Medication> droneMedications, List<MedicationDTO> medications) {
        AtomicReference<Float> medicationWeight = new AtomicReference<>((float) 0);
        AtomicReference<Float> droneCurrentWeight = new AtomicReference<>((float) 0);
        droneMedications.forEach(med -> droneCurrentWeight.updateAndGet(v -> v + med.getWeight().floatValue()));
        medications.forEach(med -> medicationWeight.updateAndGet(v -> v + med.getWeight().floatValue()));

        if (drone.getWeight().floatValue() < medicationWeight.get() + droneCurrentWeight.get()) {
            throw new IllegalArgumentException("The drone is being overloaded");
        }
    }

    public List<Drone> getAvailableDrones() {
        return droneRepository.findAllByState(State.IDLE);
    }

    public BigDecimal getDroneBatteryLevel(long droneId) {
        return droneRepository.findById(droneId).orElseThrow().getBatteryCapacity();
    }
}
