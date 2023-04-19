package ru.practicum.ewm.hit.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hits")
@Getter
@Setter
@NoArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String app;

    @Column(length = 250, nullable = false)
    private String uri;

    @Column(length = 50, nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
