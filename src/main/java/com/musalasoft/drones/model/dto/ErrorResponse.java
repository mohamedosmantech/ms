package com.musalasoft.drones.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@AllArgsConstructor
@Data
public class ErrorResponse {
    List<Error> errors;
}

