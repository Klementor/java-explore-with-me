package ru.practicum.ewm.entity.event.service.statistics;

import ru.practicum.ewm.MainServerApplication;
import ru.practicum.ewm.entity.event.entity.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

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

    default Map<Long, Long> getEventViews(Iterable<Event> events, Boolean uniqueViews) {
        Map<Long, Long> eventViews = new HashMap<>();
        LocalDateTime firstTime = events.iterator().next().getEventDate();
        List<String> uriStrings = new ArrayList<>();
        int i = 0;
        for (Event event : events) {
            if (event.getEventDate().isBefore(firstTime)) {
                firstTime = event.getEventDate();
            }
            uriStrings.add(String.format("/events%d", event.getId()));
        }
        List<Long> eventViewsList = getEventViews(firstTime,
                LocalDateTime.now(),
                uriStrings,
                uniqueViews);
        for (Event event : events) {
            eventViews.put(event.getId(), eventViewsList.get(i));
            i++;
        }
        return eventViews;
    }

    List<Long> getEventViews(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uri,
            Boolean unique
    );
}
