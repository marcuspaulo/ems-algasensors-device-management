# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.3/reference/data/sql.html#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## Wrong Serialization of TSID
```json
{
	"id": {
		"instant": "2025-10-14T10:42:0 1.521Z",
		"unixMilliseconds": 1760438521521
	},
	"name": "Sensor DF55",
	"ip": "127.0.0.1",
	"location": "Marcus Street 2066",
	"protocol": "HTTP",
	"model": "S55-HTTP",
	"enabled": false
}
```
### Fix wrong Serialization of TSID
```java
package com.algaworks.algasensors.device.management.api.config.jackson;

import com.fasterxml.jackson.databind.JsonSerializer;
import io.hypersistence.tsid.TSID;

public class TSIDToStringSerializer extends JsonSerializer<TSID> {
    
    @Override
    public void serialize(TSID value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
        gen.writeString(value.toString());
    }
}
```

```java
package com.algaworks.algasensors.device.management.api.config.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.hypersistence.tsid.TSID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TSIDJacksonConfig {
    
    @Bean
    public Module tsidModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(TSID.class, new TSIDToStringSerializer());
        return module;
    }    
}
```
### Alternative JacksonConfig
```java
@Configuration
public class TSIDJacksonConfig {
    @Bean
    public Module tsidModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(TSID.class, ToStringSerializer.instance);
        return module;
    }
}
```

## Result after added this TSID Config
```json
{
	"id": "0N87VY54M9GQR",
	"name": "Sensor DF55",
	"ip": "127.0.0.1",
	"location": "Marcus Street 2066",
	"protocol": "HTTP",
	"model": "S55-HTTP",
	"enabled": false
}
```

## Create a TSID JPA Converter
```java
package com.algaworks.algasensors.device.management.api.config.jpa;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TSIDToLongJPAAttributeConverter implements AttributeConverter<TSID, Long> {
    
    @Override
    public Long convertToDatabaseColumn(TSID attribute) {
        return attribute.toLong();
    }

    @Override
    public TSID convertToEntityAttribute(Long dbData) {
        return TSID.from(dbData);
    }
}

```