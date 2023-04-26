package ru.practicum.ewm.entity.event.service.contoller;

import ru.practicum.ewm.entity.event.dto.request.UpdateEventAdminRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventAdminService {
    Iterable<EventFullResponseDto> searchAdminEventsByParameters(
            Set<Long> users,
            Set<Event.State> states,
            Set<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size);

    EventFullResponseDto updateAdminEventById(
            Long eventId,
            UpdateEventAdminRequestDto adminRequest);
}
