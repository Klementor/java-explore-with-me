package ru.practicum.ewm.entity.event.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.event.dto.request.AddEventRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventRequestsByStatusResponseDto;
import ru.practicum.ewm.entity.event.dto.response.EventShortResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public final class EventMapper {

    public Event toEvent(AddEventRequestDto eventDto, Category category, User initiator) {
        Event event = new Event();

        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setPaid(eventDto.getPaid());
        event.setLat(eventDto.getLocation().getLat());
        event.setLon(eventDto.getLocation().getLon());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setCategory(category);
        event.setInitiator(initiator);

        return event;
    }

    public EventFullResponseDto toEventFullResponseDto(Event event, Long views, Integer confirmedRequests) {
        EventFullResponseDto eventDto = new EventFullResponseDto();

        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(EventFullResponseDto.CategoryDto.fromCategory(event.getCategory()));
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(LocalDateTime.from(event.getEventDate()));
        eventDto.setInitiator(EventFullResponseDto.UserShortDto.fromUser(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setLocation(EventFullResponseDto.Location.fromEvent(event));
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setState(event.getState());
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);

        return eventDto;
    }

    public static EventShortResponseDto toShortResponseDto(Event event,
                                                           Long views,
                                                           Integer confirmedRequests) {
        EventShortResponseDto eventDto = new EventShortResponseDto();

        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(EventShortResponseDto.CategoryDto.fromCategory(event.getCategory()));
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setEventDate(event.getEventDate());
        eventDto.setId(event.getId());
        eventDto.setInitiator(EventShortResponseDto.UserShortDto.fromUser(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(views);

        return eventDto;
    }

    public List<EventFullResponseDto> toEventFullResponseDto(
            Iterable<Event> events,
            Map<Long, Long> eventViews,
            Map<Long, Integer> eventConfirmedRequests
    ) {
        if (eventViews == null) {
            eventViews = new HashMap<>();
        }

        if (eventConfirmedRequests == null) {
            eventConfirmedRequests = new HashMap<>();
        }

        List<EventFullResponseDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            Long views = eventViews.get(event.getId());
            Integer confirmedRequests = eventConfirmedRequests.get(event.getId());
            eventsDto.add(toEventFullResponseDto(event, views, confirmedRequests));
        }
        return eventsDto;
    }

    public static EventRequestsByStatusResponseDto toEventRequestStatusUpdateResponseDto(List<Participation> requests) {
        EventRequestsByStatusResponseDto requestDto = new EventRequestsByStatusResponseDto();

        List<EventRequestsByStatusResponseDto.ParticipationDto> confirmedRequests = new ArrayList<>();
        List<EventRequestsByStatusResponseDto.ParticipationDto> rejectedRequests = new ArrayList<>();

        for (Participation request : requests) {
            var participationDto = EventRequestsByStatusResponseDto.ParticipationDto.fromParticipation(request);

            if (request.getStatus() == Participation.Status.CONFIRMED) {
                confirmedRequests.add(participationDto);
            } else if (request.getStatus() == Participation.Status.REJECTED) {
                rejectedRequests.add(participationDto);
            }
        }

        requestDto.setConfirmedRequests(confirmedRequests);
        requestDto.setRejectedRequests(rejectedRequests);

        return requestDto;
    }

    public static List<EventShortResponseDto> toShortResponseDto(
            Iterable<Event> events,
            Map<Long, Long> eventViews,
            Map<Long, Integer> eventConfirmedRequests
    ) {
        if (eventViews == null) {
            eventViews = new HashMap<>();
        }

        if (eventConfirmedRequests == null) {
            eventConfirmedRequests = new HashMap<>();
        }

        List<EventShortResponseDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            Long views = eventViews.get(event.getId());
            Integer confirmedRequests = eventConfirmedRequests.get(event.getId());
            EventShortResponseDto eventDto = toShortResponseDto(event, views, confirmedRequests);
            eventsDto.add(eventDto);
        }
        return eventsDto;
    }
}
