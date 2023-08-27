package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventFullDto {

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
     * Полное описание события
     */
    private String description;

    /**
     * Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String eventDate;

    /**
     * Широта и долгота места проведения события
     */
    private LocationDto location;

    /**
     * Нужно ли оплачивать участие
     */
    private boolean paid;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private int participantLimit;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private boolean requestModeration;

    /**
     * Количество одобренных заявок на участие в данном событии
     */
    private long confirmedRequests;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String createdOn;

    /**
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String publishedOn;

    /**
     * Пользователь (краткая информация)
     */
    private UserShortDto initiator;

    /**
     * Список состояний жизненного цикла события
     */
    private String state;

    /**
     * Количество просмотрев события
     */
    private long views;
}
