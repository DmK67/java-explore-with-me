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

    /**
     * Новый заголовок
     */
    @Size(min = 3, max = 120)
    private String title;

    /**
     * Новая аннотация
     */
    @Size(min = 20, max = 2000)
    private String annotation;

    /**
     * Новая категория
     */
    private Long category;

    /**
     * Новое описание
     */
    @Size(min = 20, max = 7000)
    private String description;

    /**
     * Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    private String eventDate;

    /**
     * Широта и долгота места проведения события
     */
    private LocationDto location;

    /**
     * Новое значение флага о платности мероприятия
     */
    private Boolean paid;

    /**
     * Новый лимит пользователей
     */
    private Integer participantLimit;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /**
     * Новое состояние события
     */
    private StateAdminAction stateAction;
}
