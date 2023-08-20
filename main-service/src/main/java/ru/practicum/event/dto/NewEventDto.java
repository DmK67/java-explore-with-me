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

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title; // Заголовок события

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation; // Краткое описание события

    @PositiveOrZero
    private long category; // id категории к которой относится событие

    @NotNull
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description; // Полное описание события

    @NotNull
    private String eventDate; // Дата и время на которые намечено событие. Дата и время указываются
    // в формате "yyyy-MM-dd HH:mm:ss"

    @NotNull
    private LocationDto location; // Широта и долгота места проведения события
    private boolean paid = false; // Нужно ли оплачивать участие в событии
    private int participantLimit = 0; // Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    private boolean requestModeration = true; // Нужна ли пре-модерация заявок на участие. Если true,
    // то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
}
