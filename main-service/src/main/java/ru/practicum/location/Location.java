package ru.practicum.location;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Широта
     */
    private float lat;

    /**
     * Долгота
     */
    private float lon;
}
