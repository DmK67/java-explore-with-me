package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private long confirmedRequests;
    private String createdOn;
    private String publishedOn;
    private UserShortDto initiator;
    private String state;
    private long views;
}
