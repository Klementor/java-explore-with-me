package ru.practicum.ewm.hit.dto.response.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViewStatsResponseDto {
    private String app;
    private String uri;
    private Long hits;
}
