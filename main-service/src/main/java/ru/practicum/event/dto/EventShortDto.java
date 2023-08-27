package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    private Long id;

    /**
     * Заголовок
     */
    private String title;

    /**
     * Краткое описание
     */
    private String annotation;

    /**
     * Категория
     */
    private CategoryDto category;

    /**
     * Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String eventDate;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private long confirmedRequests;

    /**
     * Пользователь (краткая информация)
     */
    private UserShortDto initiator;

    /**
     * Нужно ли оплачивать участие
     */
    private boolean paid;

    /**
     * Количество просмотрев события
     */
    private long views;
}
