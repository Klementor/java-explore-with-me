package ru.practicum.ewm.hit.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.EndpointHitResponseDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EndpointHitClient {
    private final WebClient webClient;
    private final DateTimeFormatter dateTimeFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EndpointHitClient(String serverUrl) {
        this.webClient = WebClient.create(serverUrl);
    }

    public EndpointHitResponseDto postEndpointHit(AddEndpointHitRequestDto endpointHitDto) {
        return webClient.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(endpointHitDto)
                .retrieve()
                .bodyToMono(EndpointHitResponseDto.class)
                .block();
    }

    public List<ViewStatsResponseDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start.format(dateTimeFormat))
                        .queryParam("end", end.format(dateTimeFormat))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStatsResponseDto.class)
                .collectList()
                .block();
    }


}
