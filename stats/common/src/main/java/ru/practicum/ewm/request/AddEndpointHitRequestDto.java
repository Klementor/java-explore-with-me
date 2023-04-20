package ru.practicum.ewm.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AddEndpointHitRequestDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    public static AddEndpointHitRequestDto toAddEndpointHitRequestDto(
            HttpServletRequest request,
            String app,
            LocalDateTime timestamp
    ) {
        AddEndpointHitRequestDto endpointHitDto = new AddEndpointHitRequestDto();

        endpointHitDto.setApp(app);
        endpointHitDto.setUri(request.getRequestURI());
        endpointHitDto.setIp(request.getRemoteAddr());
        endpointHitDto.setTimestamp(timestamp);

        return endpointHitDto;
    }
}
