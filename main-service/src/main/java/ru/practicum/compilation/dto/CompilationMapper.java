package ru.practicum.compilation.dto;

import ru.practicum.compilation.Compilation;
import ru.practicum.event.dto.EventMapper;

import java.util.Collections;
import java.util.stream.Collectors;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents() != null ?
                        compilation.getEvents().stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
