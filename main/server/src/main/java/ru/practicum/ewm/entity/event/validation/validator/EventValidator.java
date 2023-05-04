package ru.practicum.ewm.entity.event.validation.validator;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.exception.ConflictException;

import java.time.LocalDateTime;

@UtilityClass
public final class EventValidator {

    public static void validateEventDateMoreThanHourAfterPublication(Event event) {
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime publishedOn = event.getPublishedOn();
        if (eventDate == null || publishedOn == null) {
            return;
        }

        if (!eventDate.minusHours(1L).isAfter(publishedOn)) {
            throw new ConflictException("Event date to be modified must be no earlier" +
                    " than an hour from date of publication");
        }
    }

    public static void validateEventBeforeRejection(Event event) {
        if (event.getState() == Event.State.PUBLISHED) {
            throw new ConflictException("Event can be rejected only if it has not been published yet");
        }
    }

    public static void validateEventBeforePublishing(Event event) {
        if (event.getState() != Event.State.PENDING) {
            throw new ConflictException("Event can be published only if it is in the waiting state for publication");
        }
    }

    public static void validateEventDateMoreThanTwoHoursAfterCurrentTime(Event event) {
        if (!event.getEventDate().minusHours(2L).isAfter(LocalDateTime.now())) {
            throw new ConflictException("Event date cannot be earlier than two hours from the current moment");
        }
    }

    public static void validateParticipationBeforeConfirmationOrRejection(Participation participation) {
        if (participation.getStatus() != Participation.Status.PENDING) {
            throw new ConflictException("Status can only be changed for applications, being in the waiting state");
        }
    }

    public static void validateEventBeforeUpdating(Event event) {
        if (event.getState() != Event.State.CANCELED
                && event.getState() != Event.State.PENDING
        ) {
            throw new ConflictException("it is allowed to change only canceled events " +
                    "or events that are in the state of waiting for moderation");
        }
    }
}
