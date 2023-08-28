package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class StatsClient {
    private final String serverUrl;
    private final RestTemplate rest;

    public StatsClient(@Value("${app.stats.url:http://stats-server:9090}") String serverUrl) { //stats-server localhost
        this.rest = new RestTemplate();
        this.serverUrl = serverUrl;
    }

    public void addHit(HitDto hitDto) {
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto);
        rest.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public ResponseEntity<StatsDto[]> getStats(String start, String end, String[] uris, boolean unique) {
        Map<String, Object> parameters;
        String path;
        if (uris != null) {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "uris", uris,
                    "unique", unique
            );
            path = serverUrl + "/stats/?start={start}&end={end}&uris={uris}&unique={unique}";
        } else {
            parameters = Map.of(
                    "start", start,
                    "end", end,
                    "unique", unique
            );
            path = serverUrl + "/stats/?start={start}&end={end}&unique={unique}";
        }
        ResponseEntity<StatsDto[]> serverResponse = rest.getForEntity(path, StatsDto[].class, parameters);
        if (serverResponse.getStatusCode().is2xxSuccessful()) {
            return serverResponse;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(serverResponse.getStatusCode());
        if (serverResponse.hasBody()) {
            return responseBuilder.body(serverResponse.getBody());
        }
        return responseBuilder.build();
    }
}