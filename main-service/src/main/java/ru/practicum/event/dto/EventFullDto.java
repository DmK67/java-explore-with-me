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
    private String title; // Заголовок
    private String annotation; // Краткое описание
    private CategoryDto category; // Категория
    private String description; // Полное описание события
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private LocationDto location; // Широта и долгота места проведения события
    private boolean paid; // Нужно ли оплачивать участие
    private int participantLimit; // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    private long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    private String createdOn; // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String publishedOn; // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator; // Пользователь (краткая информация)
    private String state; // Список состояний жизненного цикла события
    private long views; // Количество просмотрев события
}
