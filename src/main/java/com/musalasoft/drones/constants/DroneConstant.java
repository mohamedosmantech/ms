package com.musalasoft.drones.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "drone")
@Configuration
public class DroneConstant {
    private int batteryCapacityLowLevel;
    private String batteryCheckPeriod;
}
