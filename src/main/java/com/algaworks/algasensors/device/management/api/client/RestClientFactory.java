package com.algaworks.algasensors.device.management.api.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class RestClientFactory {

    private final RestClient.Builder restClientBuilder;
    private final String baseUrl;

    public RestClientFactory(RestClient.Builder restClientBuilder,
                             @Value("${algasensors.temperature-monitoring.url}") String baseUrl) {
        this.restClientBuilder = restClientBuilder;
        this.baseUrl = baseUrl;
    }

    public RestClient temperatureMonitoringRestClient() {
        return restClientBuilder.baseUrl(baseUrl)
                .requestFactory(generateClientHttpRequestFactory())
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                }))
                .build();
    }

    private ClientHttpRequestFactory generateClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(3));
        return factory;
    }
}
