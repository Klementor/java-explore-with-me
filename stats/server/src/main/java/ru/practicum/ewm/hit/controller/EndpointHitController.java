package ru.practicum.ewm.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.response.EndpointHitResponseDto;
import ru.practicum.ewm.response.stats.ViewStatsResponseDto;
import ru.practicum.ewm.hit.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitResponseDto addEndpointHit(
            @RequestBody AddEndpointHitRequestDto endpointHitDto
    ) {
        log.info("add ENDPOINT_HIT[uri='{}', ip={}, timestamp={}].",
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());

        return endpointHitService.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsResponseDto> getStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("get VIEW_STATS[uri_count={}, unique_ip={}].",
                (uris != null) ? uris.size() : "ALL",
                unique);
        return endpointHitService.getStats(start, end, uris, unique);
    }
}
