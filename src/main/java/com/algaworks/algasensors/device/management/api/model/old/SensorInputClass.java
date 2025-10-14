package com.algaworks.algasensors.device.management.api.model.old;

import lombok.Data;

@Data
public class SensorInputClass {
    private String name;
    private String ip;
    private String location;
    private String protocol;
    private String model;
}
