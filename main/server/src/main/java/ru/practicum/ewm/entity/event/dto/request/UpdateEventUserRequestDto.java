package ru.practicum.ewm.entity.event.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.event.entity.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateEventUserRequestDto {
    @NotBlank
    @Size(min = 20, max = 2_000)
    private String annotation;

    private Long category;

    @NotBlank
    @Size(min = 20, max = 7_000)
    private String description;

    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private Event.InitiatorStateAction stateAction;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Location {
        private Float lat;
        private Float lon;
    }
}
