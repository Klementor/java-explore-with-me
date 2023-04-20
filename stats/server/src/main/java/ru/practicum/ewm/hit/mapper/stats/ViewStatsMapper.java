package ru.practicum.ewm.hit.mapper.stats;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.hit.model.stats.ViewStats;
import ru.practicum.ewm.response.stats.ViewStatsResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class ViewStatsMapper {

    public static ViewStatsResponseDto toViewStatsResponseDto(ViewStats viewStats) {
        ViewStatsResponseDto responseDto = new ViewStatsResponseDto();

        responseDto.setApp(viewStats.getApp());
        responseDto.setUri(viewStats.getUri());
        responseDto.setHits(viewStats.getHits());

        return responseDto;
    }

    public static List<ViewStatsResponseDto> toViewStatsResponseDto(List<ViewStats> viewStats) {
        return viewStats.stream()
                .map(ViewStatsMapper::toViewStatsResponseDto)
                .collect(Collectors.toList());
    }
}
