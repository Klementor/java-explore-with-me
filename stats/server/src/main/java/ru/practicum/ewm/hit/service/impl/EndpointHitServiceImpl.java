package ru.practicum.ewm.hit.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.hit.dto.request.AddEndpointHitRequestDto;
import ru.practicum.ewm.hit.dto.response.EndpointHitResponseDto;
import ru.practicum.ewm.hit.dto.response.stats.ViewStatsResponseDto;
import ru.practicum.ewm.hit.mapper.EndpointHitMapper;
import ru.practicum.ewm.hit.mapper.stats.ViewStatsMapper;
import ru.practicum.ewm.hit.model.EndpointHit;
import ru.practicum.ewm.hit.model.stats.ViewStats;
import ru.practicum.ewm.hit.repository.EndpointHitJpaRepository;
import ru.practicum.ewm.hit.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitJpaRepository endpointHitRepository;

    @Override
    @Transactional
    public EndpointHitResponseDto addEndpointHit(AddEndpointHitRequestDto endpointHitDto) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        EndpointHit savedEndpointHit = endpointHitRepository.save(endpointHit);
        log.debug("ENDPOINT_HIT[id={}, uri={}, timestamp={}] saved.",
                savedEndpointHit.getId(),
                savedEndpointHit.getUri(),
                savedEndpointHit.getTimestamp());
        return EndpointHitMapper.toEndpointHitResponseDto(savedEndpointHit);
    }

    @Override
    public List<ViewStatsResponseDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {
        List<ViewStats> endpointHitsStats;
        if (unique == Boolean.TRUE) {
            endpointHitsStats = endpointHitRepository.collectUniqueEndpointStats(
                    start,
                    end,
                    uris,
                    JpaSort.unsafe("COUNT(DISTINCT hits.ip)").descending());
        } else {
            endpointHitsStats = endpointHitRepository.collectEndpointHitStats(
                    start,
                    end,
                    uris,
                    JpaSort.unsafe("COUNT(hits.ip)").descending());
        }

        List<ViewStatsResponseDto> endpointHitsStatsDto = ViewStatsMapper.toViewStatsResponseDto(endpointHitsStats);
        log.debug("VIEW_STATS<DTO>[size={}, uri_count={}, unique_ip={}] returned.",
                endpointHitsStatsDto.size(),
                (uris != null) ? uris.size() : "ALL",
                unique);
        return endpointHitsStatsDto;
    }
}
