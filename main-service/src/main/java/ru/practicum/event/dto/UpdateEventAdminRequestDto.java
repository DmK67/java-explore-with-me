package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.StateAdminAction;

import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequestDto {

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
    private StateAdminAction stateAction;
}
