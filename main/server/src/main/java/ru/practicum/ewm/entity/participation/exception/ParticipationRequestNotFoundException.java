package ru.practicum.ewm.entity.participation.exception;

import javax.persistence.EntityNotFoundException;

public class ParticipationRequestNotFoundException extends EntityNotFoundException {
    public static final String PARTICIPATION_REQUEST_NOT_FOUND = "Request with id=%d was not found";

    public ParticipationRequestNotFoundException(String message) {
        super(message);
    }

    public static ParticipationRequestNotFoundException fromRequestId(Long requestId) {
        String message = String.format(PARTICIPATION_REQUEST_NOT_FOUND, requestId);
        return new ParticipationRequestNotFoundException(message);
    }
}
