package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequestDto {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
