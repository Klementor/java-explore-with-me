package ru.practicum.ewm.entity.participation.repository.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventRequestsCount {
    private Long eventId;
    private Integer requestsCount;

    public EventRequestsCount(Long eventId, Long requestsCount) {
        this.eventId = eventId;
        this.requestsCount = requestsCount.intValue();
    }
}
