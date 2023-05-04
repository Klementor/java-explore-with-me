package ru.practicum.ewm.entity.event.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    public enum State {
        PENDING, PUBLISHED, CANCELED
    }

    public enum AdminStateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }

    public enum InitiatorStateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }

    public enum Sort {
        EVENT_DATE, VIEWS
    }

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String title;

    @Column(length = 2_000, nullable = false)
    private String annotation;

    @Column(length = 7_000, nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private Boolean paid = Boolean.FALSE;

    @Column
    private Float lat;

    @Column
    private Float lon;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit = 0;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration = Boolean.TRUE;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state = State.PENDING;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
}
