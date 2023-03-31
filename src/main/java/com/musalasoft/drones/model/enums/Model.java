package com.musalasoft.drones.model.enums;

import lombok.Getter;

@Getter
public enum Model {

    LIGHTWEIGHT("Lightweight"),
    MIDDLEWEIGHT("Middleweight"),
    CRUISERWEIGHT("Cruiserweight"),
    HEAVYWEIGHT("Heavyweight");

    private final String code;

    Model(String code) {
        this.code = code;
    }

}
