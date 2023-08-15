package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "statistics")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String app;
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String uri;
    @Column(nullable = false, length = 40)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}
