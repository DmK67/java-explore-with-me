package ru.practicum.event;

import lombok.*;
import ru.practicum.category.Category;
import ru.practicum.compilation.Compilation;
import ru.practicum.location.Location;
import ru.practicum.request.ParticipationRequest;
import ru.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(nullable = false, name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations;

    @OneToMany(mappedBy = "event")
    private List<ParticipationRequest> requests;

    @Column(columnDefinition = "bigint default 0")
    private Long views;
}
