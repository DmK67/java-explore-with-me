package ru.practicum.compilation.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequestDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
