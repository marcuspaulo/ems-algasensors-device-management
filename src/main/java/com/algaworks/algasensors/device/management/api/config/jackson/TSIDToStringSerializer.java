package com.algaworks.algasensors.device.management.api.config.jackson;

import com.fasterxml.jackson.databind.JsonSerializer;
import io.hypersistence.tsid.TSID;

public class TSIDToStringSerializer extends JsonSerializer<TSID> {

    @Override
    public void serialize(TSID value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
        gen.writeString(value.toString());
    }
}
