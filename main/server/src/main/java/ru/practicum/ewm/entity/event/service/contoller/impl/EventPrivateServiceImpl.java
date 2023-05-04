package ru.practicum.ewm.entity.event.service.contoller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.repository.CategoryJpaRepository;
import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventUserRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventRequestsByStatusResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.mapper.EventMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.service.contoller.EventPrivateService;
import ru.practicum.ewm.entity.event.validation.validator.EventValidator;
import ru.practicum.ewm.entity.participation.dto.request.UpdateEventParticipationStatusRequestDto;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.mapper.ParticipationMapper;
import ru.practicum.ewm.entity.participation.repository.jpa.ParticipationRequestJpaRepository;
import ru.practicum.ewm.entity.participation.validation.validator.ParticipationValidator;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.repository.UserJpaRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventJpaRepository eventRepository;
    private final UserJpaRepository userRepository;
    private final CategoryJpaRepository categoryRepository;
    private final ParticipationRequestJpaRepository requestRepository;

    @Override
    @Transactional
    public EventFullResponseDto addEvent(Long userId, AddEventRequestDto eventDto) {
        User user = userRepository.checkUserExistsById(userId);
        Category category = categoryRepository.checkCategoryExistsById(eventDto.getCategory());
        Event event = getEvent(eventDto, user, category);
        EventValidator.validateEventDateMoreThanTwoHoursAfterCurrentTime(event);
        Event savedEvent = eventRepository.save(event);
        log.debug("EVENT[id={}, initiator_id={}, title='{}', event_date={}] saved.",
                event.getId(), event.getInitiator().getId(), event.getTitle(), event.getEventDate());
        return EventMapper.toEventFullResponseDto(savedEvent, null, null);
    }

    @Override
    public Iterable<EventShortResponseDto> getUserEvents(Long userId, Integer from, Integer size) {
        userRepository.checkUserExistsById(userId);
        Page<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size));
        List<EventShortResponseDto> eventDtos = EventMapper.toShortResponseDto(events, null, null);
        log.debug("EVENT_PAGE<DTO>[initiator_id={}, from={}, size={}, events_count={}] by user returned.",
                userId, from, size, eventDtos.size());
        return eventDtos;
    }

    @Override
    public EventFullResponseDto getEventById(Long userId, Long eventId) {
        userRepository.checkUserExistsById(userId);
        Event event = eventRepository.checkEventExistsById(eventId);
        EventFullResponseDto eventDto = EventMapper.toEventFullResponseDto(event, null, null);
        log.debug("EVENT<DTO>[id={}, title='{}'] returned.",
                eventDto.getId(), eventDto.getTitle());
        return eventDto;
    }

    @Override
    public Iterable<ParticipationResponseDto> getEventParticipationRequests(
            Long userId, Long eventId,
            Integer from, Integer size
    ) {
        userRepository.checkUserExistsById(userId);
        eventRepository.checkEventExistsById(eventId);
        Page<Participation> requests = requestRepository.findAllByEventInitiatorIdAndEventId(
                userId,
                eventId,
                PageRequest.of(from, size));
        List<ParticipationResponseDto> requestDtos = ParticipationMapper.toParticipationResponseDto(requests);
        log.debug("PARTICIPATION_REQUESTS_PAGE<DTO>[initiator_id={}, event_id={}, requests_count={}, from={}, size={}] by event returned.",
                userId, eventId, requestDtos.size(), from, size);
        return requestDtos;
    }

    @Override
    @Transactional
    public EventFullResponseDto updateEventById(Long userId, Long eventId, UpdateEventUserRequestDto eventDto) {
        userRepository.checkUserExistsById(userId);
        Event event = eventRepository.checkEventExistsById(eventId);
        Event updatedEvent = getUpdatedEvent(event, eventDto);
        EventValidator.validateEventBeforeUpdating(updatedEvent);
        EventValidator.validateEventDateMoreThanTwoHoursAfterCurrentTime(updatedEvent);
        performActionIfExists(updatedEvent, eventDto.getStateAction());
        log.debug("EVENT[id={}, initiator_id={}, title='{}'] updated by user.",
                updatedEvent.getId(),
                updatedEvent.getInitiator().getId(),
                updatedEvent.getTitle());
        return EventMapper.toEventFullResponseDto(updatedEvent, null, null);
    }

    @Override
    @Transactional
    public EventRequestsByStatusResponseDto updateEventParticipationRequestStatus(
            Long userId,
            Long eventId,
            UpdateEventParticipationStatusRequestDto requestStatusDto
    ) {
        userRepository.checkUserExistsById(userId);
        Event event = eventRepository.checkEventExistsById(eventId);
        List<Participation> requests = considerRequests(event, requestStatusDto);
        requestRepository.saveAll(requests);
        log.debug("EVENT_REQUESTS[request_ids_count={}, status='{}'] updated.",
                requestStatusDto.getRequestIds().size(), requestStatusDto.getStatus());
        return EventMapper.toEventRequestStatusUpdateResponseDto(requests);
    }

    private static void performActionIfExists(Event event, Event.InitiatorStateAction stateAction) {
        if (stateAction == null) {
            return;
        }

        switch (stateAction) {
            case CANCEL_REVIEW:
                event.setState(Event.State.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(Event.State.PENDING);
                break;
            default:
                String message = String.format("action %s not implemented", stateAction);
                throw new RuntimeException(message);
        }
    }

    private Event getUpdatedEvent(Event event, UpdateEventUserRequestDto eventDto) {

        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.checkCategoryExistsById(eventDto.getCategory()));
        }

        if (eventDto.getLocation() != null) {
            if (eventDto.getLocation().getLat() != null) {
                event.setLat(eventDto.getLocation().getLat());
            }
            if (eventDto.getLocation().getLon() != null) {
                event.setLon(eventDto.getLocation().getLon());
            }
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isEmpty()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        return event;
    }

    private Event getEvent(AddEventRequestDto eventDto, User initiator, Category category) {
        return EventMapper.toEvent(eventDto, category, initiator);
    }

    private List<Participation> considerRequests(
            Event event,
            UpdateEventParticipationStatusRequestDto requestStatusDto
    ) {
        int limit = event.getParticipantLimit();
        int confirmedRequests = requestRepository.getEventRequestsCount(event.getId(), Participation.Status.CONFIRMED);

        ParticipationValidator.validateLimitNotExceeded(1, confirmedRequests, limit);

        List<Participation> requests = requestRepository.findAllById(requestStatusDto.getRequestIds());
        if (requestStatusDto.getStatus() == Participation.Status.CONFIRMED) {
            for (Participation request : requests) {
                EventValidator.validateParticipationBeforeConfirmationOrRejection(request);

                // no limit
                if (event.getParticipantLimit() == 0) {
                    request.setStatus(Participation.Status.CONFIRMED);
                    continue;
                }

                if (limit - confirmedRequests > 0) {
                    request.setStatus(Participation.Status.CONFIRMED);
                    confirmedRequests++;
                } else {
                    request.setStatus(Participation.Status.REJECTED);
                }
            }
        }

        if (requestStatusDto.getStatus() == Participation.Status.REJECTED) {
            for (Participation request : requests) {
                EventValidator.validateParticipationBeforeConfirmationOrRejection(request);
                request.setStatus(Participation.Status.REJECTED);
            }
        }

        return requests;
    }
}
