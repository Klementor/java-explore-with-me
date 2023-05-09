package ru.practicum.ewm.entity.event.service.contoller;

import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventUserRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventRequestsByStatusResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.participation.dto.request.UpdateEventParticipationStatusRequestDto;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;

public interface EventPrivateService {
    EventFullResponseDto addEvent(Long userId, AddEventRequestDto eventDto);

    EventFullResponseDto getEventById(Long userId, Long eventId);

    Iterable<EventShortResponseDto> getUserEvents(Long userId, Integer from, Integer size);

    Iterable<ParticipationResponseDto> getEventParticipationRequests(
            Long userId,
            Long eventId,
            Integer from,
            Integer size);

    EventFullResponseDto updateEventById(
            Long userId,
            Long eventId,
            UpdateEventUserRequestDto eventDto);

    EventRequestsByStatusResponseDto updateEventParticipationRequestStatus(
            Long userId,
            Long eventId,
            UpdateEventParticipationStatusRequestDto requestStatusDto);
}
