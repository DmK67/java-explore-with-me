package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.StateAdminAction;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Size;

/**
 * Данные для изменения информации о событии.
 * Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequestDto {

    @Size(min = 3, max = 120)
    private String title; // Новый заголовок

    @Size(min = 20, max = 2000)
    private String annotation; // Новая аннотация
    private Long category; // Новая категория

    @Size(min = 20, max = 7000)
    private String description; // Новое описание
    private String eventDate; // Новые дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private LocationDto location; // Широта и долгота места проведения события
    private Boolean paid; // Новое значение флага о платности мероприятия
    private Integer participantLimit; // Новый лимит пользователей
    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие
    private StateAdminAction stateAction; // Новое состояние события
}
