package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    /**
     * Заголовок события
     */
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    /**
     * Краткое описание события
     */
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    /**
     * id категории к которой относится событие
     */
    @PositiveOrZero
    private long category;

    /**
     * Полное описание события
     */
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    /**
     * Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    @NotNull
    private String eventDate;

    /**
     * Широта и долгота места проведения события
     */
    @NotNull
    private LocationDto location;

    /**
     * Нужно ли оплачивать участие в событии
     */
    private boolean paid = false;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private int participantLimit = 0;

    /**
     * Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором
     * события. Если false - то будут подтверждаться автоматически.
     */
    private boolean requestModeration = true;
}
