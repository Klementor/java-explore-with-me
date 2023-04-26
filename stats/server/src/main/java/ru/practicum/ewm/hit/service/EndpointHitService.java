package ru.practicum.ewm.hit.service;

import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.EndpointHitResponseDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    EndpointHitResponseDto addEndpointHit(AddEndpointHitRequestDto endpointHitDto);

    List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
