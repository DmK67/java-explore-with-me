package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
