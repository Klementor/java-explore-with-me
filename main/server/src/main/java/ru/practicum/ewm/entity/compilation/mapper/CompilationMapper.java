package ru.practicum.ewm.entity.compilation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.entity.compilation.dto.request.AddCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto.EventShortDto;
import ru.practicum.ewm.entity.compilation.entity.Compilation;
import ru.practicum.ewm.entity.event.entity.Event;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {

    public static Compilation toCompilation(AddCompilationRequestDto compilationDto,
                                            Set<Event> events) {
        Compilation compilation = new Compilation();
        if (!compilationDto.getTitle().isBlank()) {
            compilation.setTitle(compilationDto.getTitle());
        }
        compilation.setPinned(compilationDto.isPinned());
        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }

        return compilation;
    }

    public static CompilationResponseDto toCompilationResponseDto(Compilation compilation,
                                                                  Map<Long, Long> eventViews,
                                                                  Map<Long, Long> confirmedRequests) {
        if (eventViews == null) {
            eventViews = new HashMap<>();
        }

        if (confirmedRequests == null) {
            confirmedRequests = new HashMap<>();
        }

        CompilationResponseDto compilationDto = new CompilationResponseDto();

        List<EventShortDto> events = toEventShortDto(compilation.getEvents(), eventViews, confirmedRequests);

        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(events);

        return compilationDto;
    }

    public static List<CompilationResponseDto> toCompilationResponseDto(Iterable<Compilation> compilations,
                                                                        Map<Long, Long> eventViews,
                                                                        Map<Long, Long> confirmedRequests) {
        List<CompilationResponseDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            compilationDtos.add(toCompilationResponseDto(compilation, eventViews, confirmedRequests));
        }
        return compilationDtos;
    }

    private static List<EventShortDto> toEventShortDto(Set<Event> events,
                                                Map<Long, Long> eventViews,
                                                Map<Long, Long> eventConfirmedRequests) {
        List<EventShortDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            Long views = eventViews.get(event.getId());
            Long confirmedRequests = eventConfirmedRequests.get(event.getId());

            eventDtos.add(toEventShortDto(event, views, confirmedRequests));
        }
        return eventDtos;
    }

    private static EventShortDto toEventShortDto(Event event,
                                          Long views,
                                          Long confirmedRequests) {
        EventShortDto eventDto = EventShortDto.fromEvent(event);

        eventDto.setViews(views);
        eventDto.setConfirmedRequests(confirmedRequests);

        return eventDto;
    }
}
