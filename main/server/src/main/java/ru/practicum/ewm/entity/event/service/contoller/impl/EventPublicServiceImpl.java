package ru.practicum.ewm.entity.event.service.contoller.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.mapper.EventMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.service.contoller.EventPublicService;
import ru.practicum.ewm.entity.event.service.statistics.EventStatisticsService;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.repository.jpa.ParticipationRequestJpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {
    private final EventJpaRepository eventRepository;
    private final ParticipationRequestJpaRepository requestRepository;
    private final EventStatisticsService eventStatisticsService;

    @Override
    public EventFullResponseDto getEventById(Long id, HttpServletRequest request) {
        eventRepository.checkEventExistsById(id);
        Event event = eventRepository.getReferenceById(id);
        eventStatisticsService.addEventView(request, LocalDateTime.now());
        EventFullResponseDto eventDto = getEventFullResponseDto(event, request);
        log.debug("EVENT<DTO>[id={}, title='{}'] returned.",
                eventDto.getId(), eventDto.getTitle());
        return eventDto;
    }

    @Override
    public Iterable<EventShortResponseDto> searchEventsByParameters(
            String text,
            Set<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            Event.Sort sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(from, size);
        Iterable<Event> events = eventRepository.searchEventsByParameters(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                Event.State.PUBLISHED,
                pageable);

        if (onlyAvailable == Boolean.TRUE) {
            events = getOnlyAvailableEvents(events);
        }

        eventStatisticsService.addEventView(request, LocalDateTime.now());

        List<EventShortResponseDto> eventDtos = EventMapper.toShortResponseDto(
                events,
                eventStatisticsService.getEventViews(events, false),
                requestRepository.getEventRequestsCount(events, Participation.Status.CONFIRMED));

        if (sort != null) {
            eventDtos = sortEvents(sort, eventDtos);
        }

        log.debug("EVENT_PAGE<DTO>[events_count={}, from={}, size={}, sort_by={}] by parameters returned.",
                eventDtos.size(), from, size, sort);
        return eventDtos;
    }

    private static List<EventShortResponseDto> sortEvents(
            @NonNull Event.Sort sort,
            Iterable<EventShortResponseDto> eventDtos
    ) {
        List<EventShortResponseDto> sortedEventDtos;
        switch (sort) {
            case EVENT_DATE:
                sortedEventDtos = StreamSupport.stream(eventDtos.spliterator(), false)
                        .sorted(Comparator.comparing(EventShortResponseDto::getEventDate).reversed())
                        .collect(Collectors.toList());
                break;
            case VIEWS:
                sortedEventDtos = StreamSupport.stream(eventDtos.spliterator(), false)
                        .sorted(Comparator.comparing(EventShortResponseDto::getViews).reversed())
                        .collect(Collectors.toList());
                break;
            default:
                throw new RuntimeException(String.format("sorting %s not implemented", sort));
        }
        return sortedEventDtos;
    }

    private List<Event> getOnlyAvailableEvents(Iterable<Event> events) {
        List<Event> availableEvents = new ArrayList<>();

        Map<Long, Integer> confirmedRequestsByEventId =
                requestRepository.getEventRequestsCount(events, Participation.Status.CONFIRMED);

        for (Event event : events) {
            if (confirmedRequestsByEventId.get(event.getId()) < event.getParticipantLimit()) {
                availableEvents.add(event);
            }
        }
        return availableEvents;
    }

    private EventFullResponseDto getEventFullResponseDto(Event event, HttpServletRequest request) {
        return EventMapper.toEventFullResponseDto(event,
                eventStatisticsService.getEventViews(
                        LocalDateTime.from(event.getEventDate()).minusYears(1L),
                        LocalDateTime.from(event.getEventDate()),
                        null,
                        request),
                requestRepository.getEventRequestsCount(
                        event.getId(),
                        Participation.Status.CONFIRMED));
    }
}
