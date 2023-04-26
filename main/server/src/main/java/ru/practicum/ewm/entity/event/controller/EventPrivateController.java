package ru.practicum.ewm.entity.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventUserRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventRequestsByStatusResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.service.contoller.EventPrivateService;
import ru.practicum.ewm.entity.participation.dto.request.UpdateEventParticipationStatusRequestDto;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventPrivateService privateEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullResponseDto addEvent(@PathVariable Long userId,
                                         @RequestBody @Valid AddEventRequestDto eventDto) {
        log.info("add EVENT[title='{}'].", eventDto.getTitle());
        return privateEventService.addEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullResponseDto getEventById(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info("get EVENT[initiator_id={}, id={}].", userId, eventId);
        return privateEventService.getEventById(userId, eventId);
    }

    @GetMapping
    public Iterable<EventShortResponseDto> getUserEvents(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0")
                                                             @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "10")
                                                             @Positive Integer size) {
        log.info("get EVENT_PAGE<DTO>[initiator_id={}, from={}, size={}] by initiator.",
                userId, from, size);
        return privateEventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public Iterable<ParticipationResponseDto> getEventParticipationRequests(@PathVariable Long userId,
                                                                            @PathVariable Long eventId,
                                                                            @RequestParam(defaultValue = "0")
                                                                                @PositiveOrZero Integer from,
                                                                            @RequestParam(defaultValue = "10")
                                                                                @Positive Integer size) {
        log.info("get PARTICIPATION_REQUEST_PAGE<DTO>[initiator_id={}, event_id={}, from={}, size={}] by event.",
                userId, eventId, from, size);
        return privateEventService.getEventParticipationRequests(userId, eventId, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponseDto updateUserEventById(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @RequestBody @Valid UpdateEventUserRequestDto eventDto) {
        log.info("update EVENT[initiator_id={}, event_id={}, state_action={}].",
                userId, eventId, eventDto.getStateAction());
        return privateEventService.updateEventById(userId, eventId, eventDto);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestsByStatusResponseDto updateEventParticipationRequestStatus(@PathVariable Long userId,
                                                                                  @PathVariable Long eventId,
                                                                                  @RequestBody
                                                                                      @Valid UpdateEventParticipationStatusRequestDto requestStatusDto) {
        log.info("update EVENT_PARTICIPATION_REQUEST[initiator_id={}, event_id={}, request_ids_count={}, status='{}'].",
                userId, eventId, requestStatusDto.getRequestIds().size(), requestStatusDto.getStatus());
        return privateEventService.updateEventParticipationRequestStatus(userId, eventId, requestStatusDto);
    }
}
