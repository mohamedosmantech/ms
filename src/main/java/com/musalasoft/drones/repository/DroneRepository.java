package com.musalasoft.drones.repository;

import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);

    List<Drone> findAllByState(State state);
}
