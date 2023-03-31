package com.musalasoft.drones.model.dto;

import com.musalasoft.drones.model.enums.Model;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class DroneDTO {

    @NotBlank
    @Size(max = 100)
    private String serialNumber;

    @NotNull
    private Model model;

    @DecimalMax(value = "500")
    private BigDecimal weight;

    @DecimalMax(value = "100")
    private BigDecimal batteryCapacity;
}
