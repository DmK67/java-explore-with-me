package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events; // Список идентификаторов событий входящих в подборку
    private Boolean pinned = false; // Закреплена ли подборка на главной странице сайта

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title; // Заголовок подборки
}
