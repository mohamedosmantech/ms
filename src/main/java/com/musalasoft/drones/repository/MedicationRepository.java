package com.musalasoft.drones.repository;


import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findAllByDroneAndLoadingSession(Drone drone, String loadingSession);
}