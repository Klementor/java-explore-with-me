package ru.practicum.ewm.entity.event.service.contoller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.repository.CategoryJpaRepository;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventAdminRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.mapper.EventMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.service.contoller.EventAdminService;
import ru.practicum.ewm.entity.event.service.statistics.EventStatisticsService;
import ru.practicum.ewm.entity.event.validation.validator.EventValidator;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.repository.jpa.ParticipationRequestJpaRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    private final EventJpaRepository eventRepository;
    private final CategoryJpaRepository categoryRepository;
    private final ParticipationRequestJpaRepository requestRepository;
    private final EventStatisticsService eventStatisticsService;

    @Override
    public Iterable<EventFullResponseDto> searchAdminEventsByParameters(
            Set<Long> users,
            Set<Event.State> states,
            Set<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Page<Event> events = eventRepository.searchEventsByAdminParameters(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from, size));

        List<EventFullResponseDto> eventDtos = EventMapper.toEventFullResponseDto(
                events,
                eventStatisticsService.getEventViews(events, false),
                requestRepository.getEventRequestsCount(events, Participation.Status.CONFIRMED));
        eventDtos.sort(Comparator.comparing(EventFullResponseDto::getId).reversed());
        log.debug("EVENT_PAGE<DTO>[from={}, size={}, events_count={}] by parameters returned.",
                from, size, eventDtos.size());
        return eventDtos;
    }

    @Override
    @Transactional
    public EventFullResponseDto updateAdminEventById(Long eventId, UpdateEventAdminRequestDto adminRequest) {

        Event event = getUpdatedEvent(eventRepository.checkEventExistsById(eventId), adminRequest);
        checkEventAdminUpdate(event, adminRequest.getStateAction());
        performActionIfExists(event, adminRequest.getStateAction());
        log.debug("EVENT[id={}, initiator_id={}, title='{}'] updated by admin.",
                event.getId(), event.getInitiator().getId(), event.getTitle());
        return EventMapper.toEventFullResponseDto(event, null, null);
    }

    private static void checkEventAdminUpdate(Event event, Event.AdminStateAction stateAction) {
        EventValidator.validateEventDateMoreThanHourAfterPublication(event);

        if (stateAction == Event.AdminStateAction.PUBLISH_EVENT) {
            EventValidator.validateEventBeforePublishing(event);
        } else if (stateAction == Event.AdminStateAction.REJECT_EVENT) {
            EventValidator.validateEventBeforeRejection(event);
        }
    }

    private static void performActionIfExists(Event event, Event.AdminStateAction stateAction) {
        if (stateAction == null) {
            return;
        }

        switch (stateAction) {
            case PUBLISH_EVENT:
                event.setState(Event.State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                event.setState(Event.State.CANCELED);
                break;
            default:
                throw new RuntimeException(String.format("Action '%s' not implemented", stateAction));
        }
    }

    private Event getUpdatedEvent(Event event, UpdateEventAdminRequestDto adminRequest) {

        if (adminRequest.getCategory() != null) {
            Category category = categoryRepository.checkCategoryExistsById(adminRequest.getCategory());
            event.setCategory(category);
        }

        if (adminRequest.getLocation() != null) {
            if (adminRequest.getLocation().getLat() != null) {
                event.setLat(adminRequest.getLocation().getLat());
            }
            if (adminRequest.getLocation().getLon() != null) {
                event.setLon(adminRequest.getLocation().getLon());
            }
        }
        if (adminRequest.getTitle() == null || !adminRequest.getTitle().isBlank()) {
            event.setTitle(adminRequest.getTitle());
        }
        if (adminRequest.getAnnotation() == null || !adminRequest.getAnnotation().isBlank()) {
            event.setAnnotation(adminRequest.getAnnotation());
        }
        if (adminRequest.getDescription() == null || !adminRequest.getDescription().isBlank()) {
            event.setDescription(adminRequest.getDescription());
        }
        if (adminRequest.getEventDate() != null) {
            event.setEventDate(adminRequest.getEventDate());
        }
        if (adminRequest.getPaid() != null) {
            event.setPaid(adminRequest.getPaid());
        }
        if (adminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminRequest.getParticipantLimit());
        }
        if (adminRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminRequest.getRequestModeration());
        }

        return event;
    }
}
