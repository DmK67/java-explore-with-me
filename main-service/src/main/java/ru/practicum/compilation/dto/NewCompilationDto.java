package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    /**
     * Список идентификаторов событий входящих в подборку
     */
    private List<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned = false;

    /**
     * Заголовок подборки
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
