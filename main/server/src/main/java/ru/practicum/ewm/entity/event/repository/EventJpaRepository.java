package ru.practicum.ewm.entity.event.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.exception.EventNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query(value = ""
            + "SELECT events "
            + "FROM Event AS events "
            + "WHERE (((:userIds) IS NULL) OR (events.initiator.id IN (:userIds))) "
            + "  AND (((:states) IS NULL) OR (events.state IN (:states))) "
            + "  AND (((:categoryIds) IS NULL) OR (events.category.id IN (:categoryIds))) "
            + "  AND ((CAST(:start AS date) IS NULL) OR (events.eventDate >= :start)) "
            + "  AND ((CAST(:end AS date) IS NULL) OR (events.eventDate <= :end)) ")
    Page<Event> searchEventsByAdminParameters(
            @Param("userIds") Set<Long> userIds,
            @Param("states") Set<Event.State> states,
            @Param("categoryIds") Set<Long> categoryIds,
            @Param("start") LocalDateTime rangeStart,
            @Param("end") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query(value = ""
            + "SELECT events "
            + "FROM Event as events "
            + "WHERE (:text IS NULL "
            + "      OR LOWER(events.description) LIKE LOWER(CONCAT('%', :text, '%')) "
            + "      OR LOWER(events.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) "
            + "  AND (:state IS NULL OR events.state = (:state)) "
            + "  AND (:paid IS NULL OR events.paid = :paid) "
            + "  AND ((:categoryIds) IS NULL OR events.category.id IN (:categoryIds)) "
            + "  AND (CAST(:start AS date) IS NULL OR events.eventDate >= :start) "
            + "  AND (CAST(:end AS date) IS NULL OR events.eventDate <= :end) ")
    Page<Event> searchEventsByParameters(
            @Param("text") String text,
            @Param("categoryIds") Set<Long> categoryIds,
            @Param("paid") Boolean paid,
            @Param("start") LocalDateTime rangeStart,
            @Param("end") LocalDateTime rangeEnd,
            @Param("state") Event.State state,
            Pageable pageable);

    default void checkEventExistsById(@NonNull Long eventId) {
        if (!existsById(eventId)) {
            throw EventNotFoundException.fromEventId(eventId);
        }
    }
}
