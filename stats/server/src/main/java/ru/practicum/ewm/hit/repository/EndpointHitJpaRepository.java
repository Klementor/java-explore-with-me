package ru.practicum.ewm.hit.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.hit.model.stats.ViewStats;
import ru.practicum.ewm.hit.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitJpaRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = ""
            + "SELECT "
            + "  NEW ru.practicum.ewm.hit.model.stats.ViewStats( "
            + "    hits.app, "
            + "    hits.uri, "
            + "    COUNT(hits.ip) "
            + "  ) "
            + "FROM "
            + "  EndpointHit AS hits "
            + "WHERE "
            + "    (((:uris) IS NULL) OR (hits.uri IN (:uris))) "
            + "  AND "
            + "    ((CAST(:start AS date) IS NULL) OR (hits.timestamp >= :start)) "
            + "  AND "
            + "    ((CAST(:end AS date) IS NULL) OR (hits.timestamp <= :end)) "
            + "GROUP BY "
            + "  hits.app, "
            + "  hits.uri ")
    List<ViewStats> collectEndpointHitStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris,
            Sort sort);

    @Query(value = ""
            + "SELECT "
            + "  NEW ru.practicum.ewm.hit.model.stats.ViewStats( "
            + "    hits.app, "
            + "    hits.uri, "
            + "    COUNT(DISTINCT hits.ip) "
            + "  ) "
            + "FROM "
            + "  EndpointHit AS hits "
            + "WHERE "
            + "    (((:uris) IS NULL) OR (hits.uri IN (:uris))) "
            + "  AND "
            + "    ((CAST(:start AS date) IS NULL) OR (hits.timestamp >= :start)) "
            + "  AND "
            + "    ((CAST(:end AS date) IS NULL) OR (hits.timestamp <= :end)) "
            + "GROUP BY "
            + "  hits.app, "
            + "  hits.uri ")
    List<ViewStats> collectUniqueEndpointStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris,
            Sort sort);
}
