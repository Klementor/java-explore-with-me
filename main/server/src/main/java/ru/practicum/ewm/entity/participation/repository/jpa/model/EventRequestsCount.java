package ru.practicum.ewm.entity.participation.repository.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.entity.participation.entity.Participation;

@AllArgsConstructor
@Getter
@Setter
public class EventRequestsCount {
    private Long eventId;
    private Long requestsCount;
    private Participation.Status status;
}
