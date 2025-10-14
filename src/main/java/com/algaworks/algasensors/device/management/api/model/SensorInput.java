package com.algaworks.algasensors.device.management.api.model;

import jakarta.validation.constraints.NotNull;

public record SensorInput(
        @NotNull String name,
        @NotNull String ip,
        @NotNull String location,
        @NotNull String protocol,
        @NotNull String model) {
}
