package com.stcu.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordenadaDTO implements Serializable {

    private double lat;

    private double lng;

    public CoordenadaDTO() {
    }

    public CoordenadaDTO(double x, double y) {
        this.lat = x;
        this.lng = y;
    }
}
