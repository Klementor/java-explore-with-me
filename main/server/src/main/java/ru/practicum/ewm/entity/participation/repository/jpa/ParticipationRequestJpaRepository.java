package ru.practicum.ewm.entity.participation.repository.jpa;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.entity.Participation.Status;
import ru.practicum.ewm.entity.participation.exception.ParticipationRequestNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public interface ParticipationRequestJpaRepository extends JpaRepository<Participation, Long> {

    @Query(""
            + "SELECT COUNT(*) "
            + "FROM Participation AS requests "
            + "WHERE requests.event.id = ?1 "
            + "AND requests.status = ?2 ")
    Integer getEventRequestsCount(Long eventId, Status requestStatus);

//    @Query(""
//            + "SELECT "
//            + "  new ru.practicum.ewm.entity.participation.repository.jpa.model.EventRequestsCount( "
//            + "    requests.event.id, COUNT(*), requests.status "
//            + "  ) "
//            + "FROM Participation AS requests "
//            + "WHERE ((:eventIds) IS NULL OR requests.event.id IN (:eventIds)) "
//            + "AND (requests.status = :status) "
//            + "GROUP BY requests.id ")
//    List<EventRequestsCount> getEventsRequestsCount(@Param("eventIds") Set<Long> eventIds,
//                                                    @Param("status") Status requestStatus);

    List<Participation> findAllByRequesterId(Long userId);

    Page<Participation> findAllByEventInitiatorIdAndEventId(Long userId, Long eventId, Pageable pageable);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    default Participation checkParticipationExistsById(@NonNull Long requestId) {
        try {
            return getReferenceById(requestId);
        } catch (Exception e) {
            throw ParticipationRequestNotFoundException.fromRequestId(requestId);
        }
    }

    default Map<Long, Integer> getEventRequestsCount(Iterable<Event> events, Status requestStatus) {
        Set<Long> eventIds = StreamSupport.stream(events.spliterator(), false)
                .map(Event::getId)
                .collect(Collectors.toSet());

        return getEventRequestsCount(eventIds, requestStatus);
    }

    default Map<Long, Integer> getEventRequestsCount(Set<Long> eventIds, Status requestStatus) {
//        Map<Long, Integer> eventRequestsCount = new HashMap<>();

//        for (Long eventId : eventIds) {
//            eventRequestsCount.put(eventId, 0);
//        }
//
//        for (EventRequestsCount requestsCount : getEventsRequestsCount(eventIds, requestStatus)) {
//            eventRequestsCount.put(requestsCount.getEventId(),
//            requestsCount.getRequestsCount().intValue());
//        }
        return getEventRequests(eventIds, requestStatus);
    }

    @Query("select req.event.id, count(req) " +
            "from Participation req " +
            "where req.event.id = (:eventIds) " +
            "and req.status = (:status) " +
            "group by req.event.id")
    Map<Long, Integer> getEventRequests(@Param("eventIds") Set<Long> eventIds,
                                        @Param("status") Status requestStatus);
}
