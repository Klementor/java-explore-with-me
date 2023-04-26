package ru.practicum.ewm.entity.event.service.statistics;

import ru.practicum.ewm.MainServerApplication;
import ru.practicum.ewm.entity.event.entity.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public interface EventStatisticsService {
    String APP_NAME = MainServerApplication.class.getSimpleName();

    void addEventView(HttpServletRequest request, LocalDateTime timestamp);

    Long getEventViews(LocalDateTime start, LocalDateTime end, String uri, Boolean unique);

    default Long getEventViews(LocalDateTime start,
                               LocalDateTime end,
                               Boolean uniqueViews,
                               HttpServletRequest request) {
        return getEventViews(start, end, request.getRequestURI(), uniqueViews);
    }

    default Long getEventViews(LocalDateTime start,
                               LocalDateTime end,
                               Boolean uniqueViews,
                               String uri) {
        return getEventViews(start, end, uri, uniqueViews);
    }

    default Map<Long, Long> getEventViews(Iterable<Event> events, Boolean uniqueViews) {
        Map<Long, Long> eventViews = new HashMap<>();

        for (Event event : events) {
            Long views = getEventViews(
                    event.getEventDate().minusYears(1L),
                    event.getEventDate(),
                    uniqueViews,
                    "/events" + event.getId());
            eventViews.put(event.getId(), views);
        }
        return eventViews;
    }
}
