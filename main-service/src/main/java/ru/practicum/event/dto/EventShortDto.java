package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private String eventDate;
    private long confirmedRequests;
    private UserShortDto initiator;
    private boolean paid;
    private long views;
}
