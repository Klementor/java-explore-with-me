package ru.practicum.ewm.entity.event.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.event.entity.Event;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateEventAdminRequestDto {
    @Size(min = 20, max = 2_000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7_000)
    private String description;

    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;

    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration;
    private Event.AdminStateAction stateAction;

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
