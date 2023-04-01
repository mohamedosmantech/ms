package com.musalasoft.drones.model.entities;


import com.musalasoft.drones.model.enums.Model;
import com.musalasoft.drones.model.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@SuperBuilder
@NoArgsConstructor
@Data
@Entity
@EnableJpaAuditing
public class Drone {

    @Id
    @GeneratedValue
    private long droneId;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String serialNumber;

    @NotNull
    private Model model;

    @DecimalMax(value = "500")
    private BigDecimal weight;

    @DecimalMax(value = "100")
    private BigDecimal batteryCapacity;

    @NotNull
    private State state;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "medicationId")
    private List<Medication> medications;
    @CreationTimestamp
    private Instant createdDate;

    @UpdateTimestamp
    private Instant lastModifiedDate;

    private String activeLoadingSession;

}

