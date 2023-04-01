package com.musalasoft.drones.model.mapper;


import com.musalasoft.drones.model.dto.DroneDTO;
import com.musalasoft.drones.model.dto.MedicationDTO;
import com.musalasoft.drones.model.entities.Drone;
import com.musalasoft.drones.model.entities.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DroneMapper {
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "activeLoadingSession", ignore = true)
    Drone droneDTOToDroneEntity(DroneDTO droneDTO);

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "loadingSession", ignore = true)
    Medication medicationDTOToMedicationEntity(MedicationDTO medicationDTO);
}

