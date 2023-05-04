package ru.practicum.ewm.entity.participation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.participation.entity.Participation;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class ParticipationResponseDto {
    private Long id;
    private Long requester;
    private Long event;
    private Participation.Status status;
    private LocalDateTime created;
}
