package com.musalasoft.drones.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@Data
public class MedicationDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9\\-_]+$")
    private String name;

    @NotNull
    private BigDecimal weight;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9_]+$")
    private String code;

    @NotBlank
    private String image;
}
