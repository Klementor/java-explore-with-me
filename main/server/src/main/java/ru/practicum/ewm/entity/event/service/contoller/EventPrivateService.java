package ru.practicum.ewm.entity.event.service.contoller;

import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventUserRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.UpdateCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventRequestsByStatusResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.participation.dto.request.UpdateEventParticipationStatusRequestDto;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;

public interface EventPrivateService {
    EventFullResponseDto addEvent(Long userId, AddEventRequestDto eventDto);

    EventFullResponseDto getEventById(Long userId, Long eventId);


    CommentResponseDto addComment(Long userId, Long eventId, AddCommentRequestDto commentDto);

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


    CommentResponseDto updateCommentById(
            Long userId,
            Long eventId,
            Long comId,
            UpdateCommentRequestDto commentDto
    );

    EventRequestsByStatusResponseDto updateEventParticipationRequestStatus(
            Long userId,
            Long eventId,
            UpdateEventParticipationStatusRequestDto requestStatusDto);


    void deleteCommentById(Long userId, Long eventId, Long comId);
}
