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
    private String title; // Заголовок
    private String annotation; // Краткое описание
    private CategoryDto category; // Категория
    private String eventDate; // Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
    private long confirmedRequests; // Количество одобренных заявок на участие в данном событии
    private UserShortDto initiator; // Пользователь (краткая информация)
    private boolean paid; // Нужно ли оплачивать участие
    private long views; // Количество просмотрев события
}
