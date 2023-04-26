package ru.practicum.ewm.entity.event.exception;

import javax.persistence.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {
    public static final String EVENT_NOT_FOUND = "Event with id=%d was not found";

    public EventNotFoundException(String message) {
        super(message);
    }

    public static EventNotFoundException fromEventId(Long eventId) {
        String message = String.format(EVENT_NOT_FOUND, eventId);
        return new EventNotFoundException(message);
    }
}
