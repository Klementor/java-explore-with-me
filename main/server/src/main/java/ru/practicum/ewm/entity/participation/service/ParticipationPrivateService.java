package ru.practicum.ewm.entity.participation.service;

import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;

public interface ParticipationPrivateService {
    ParticipationResponseDto addRequest(Long userId, Long eventId);

    Iterable<ParticipationResponseDto> getRequestsByRequesterId(Long userId);

    ParticipationResponseDto cancelRequestById(Long userId, Long requestId);
}
