package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.StateUserAction;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequestDto {

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateUserAction stateAction;
}
