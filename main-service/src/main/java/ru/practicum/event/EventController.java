package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

    /**
     * POST /users/{userId}/events Добавление нового события. Обратите внимание: дата и время на которые намечено
     * событие не может быть раньше, чем через два часа от текущего момента.
     * userId - id текущего пользователя
     * Request body - данные добавляемого события
     * Responses:
     * 201 - Событие добавлено. Example:
     * 400 - Запрос составлен некорректно
     * 409 - Событие не удовлетворяет правилам создания
     */
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @Valid @Positive Long userId,
                                    @RequestBody @Validated NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    /**
     * GET /users/{userId}/events Получение событий, добавленных текущим пользователем. В случае, если по заданным
     * фильтрам не найдено ни одного события, возвращает пустой список.
     * userId - id текущего пользователя
     * from - количество элементов, которые нужно пропустить для формирования текущего набора. Default value : 0
     * size - количество элементов в наборе. Default value : 10
     * Responses:
     * 200 - События найдены
     * 400 - Запрос составлен некорректно.
     */
    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable @Positive Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getEvents(userId, from, size);
    }

    /**
     * GET /users/{userId}/events/{eventId} Получение полной информации о событии добавленном текущим пользователем.
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     * userId - id текущего пользователя
     * eventId - id события
     * Responses:
     * 200 - Событие найдено
     * 400 - Запрос составлен некорректно
     * 404 - Событие не найдено или недоступно
     */
    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable @Positive Long userId,
                                     @PathVariable @Positive Long eventId) {
        return eventService.getEventById(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/events/{eventId} Изменение события добавленного текущим пользователем.
     * Обратите внимание:
     * изменить можно только отмененные события или события в состоянии ожидания модерации(Ожидается код ошибки 409);
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего
     * момента (Ожидается код ошибки 409).
     * userId - id текущего пользователя
     * eventId - id редактируемого события
     * Request body - Новые данные события
     * Responses:
     * 200 - Событие обновлено
     * 400 - Запрос составлен некорректно
     * 404 - Событие не найдено или недоступно
     * 409 - Событие не удовлетворяет правилам редактирования
     */
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable @Positive Long userId,
                                          @PathVariable @Positive Long eventId,
                                          @RequestBody @Validated UpdateEventUserRequestDto updateEventUserRequestDto) {
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequestDto);
    }

    /**
     * GET /admin/events Поиск событий. Эндпоинт возвращает полную информацию обо всех событиях подходящих
     * под переданные условия. В случае, если по заданным фильтрам не найдено ни одного события,
     * возвращает пустой список.
     * users - список id пользователей, чьи события нужно найти
     * states - список состояний в которых находятся искомые события
     * categories - список id категорий в которых будет вестись поиск
     * rangeStart - дата и время не раньше которых должно произойти событие
     * rangeStart, rangeEnd - дата и время не позже которых должно произойти событие
     * from - количество событий, которые нужно пропустить для формирования текущего набора. Default value : 0
     * size - количество событий в наборе. Default value : 10
     * Responses:
     * 200 - События найдены
     * 400 - Запрос составлен некорректно
     */
    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * PATCH /admin/events/{eventId} Редактирование данных события и его статуса (отклонение/публикация).
     * Редактирование данных любого события администратором. Валидация данных не требуется.
     * Обратите внимание:
     * дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409);
     * событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409);
     * событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409).
     * eventId - id события
     * Request body - Данные для изменения информации о событии
     * 200 - Событие отредактировано
     * 404 - Событие не найдено или недоступно
     * 409 - Событие не удовлетворяет правилам редактирования
     */
    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody @Validated UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequestDto);
    }

    /**
     * GET /events Получение событий с возможностью фильтрации. Обратите внимание:
     * это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события;
     * текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв если в запросе
     * не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты
     * и времени;
     * информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных
     * заявок на участие;
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить
     * в сервисе статистики. В случае, если по заданным фильтрам не найдено ни одного события, возвращает
     * пустой список.
     * text - текст для поиска в содержимом аннотации и подробном описании события
     * categories - список идентификаторов категорий в которых будет вестись поиск
     * paid - поиск только платных/бесплатных событий
     * rangeStart - дата и время не раньше которых должно произойти событие
     * rangeEnd - дата и время не позже которых должно произойти событие
     * onlyAvailable - только события у которых не исчерпан лимит запросов на участие. Default value : false
     * sort - Вариант сортировки: по дате события или по количеству просмотров. Available values : EVENT_DATE, VIEWS
     * from - количество событий, которые нужно пропустить для формирования текущего набора. Default value : 0
     * size - количество событий в наборе. Default value : 10
     * Responses:
     * 200 - События найдены
     * 400 - Запрос составлен некорректно
     */
    @GetMapping("/events")
    public List<EventShortDto> getPublishedEvents(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                  HttpServletRequest request) {
        return eventService.getPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    /**
     * GET /events/{id} Получение подробной информации об опубликованном событии по его идентификатору
     * Обратите внимание:
     * событие должно быть опубликовано; информация о событии должна включать в себя количество просмотров
     * и количество подтвержденных запросов;
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе
     * статистики. В случае, если события с заданным id не найдено, возвращает статус код 404.
     * id - id события
     * Responses:
     * 200 - Событие найдено
     * 400 - Запрос составлен некорректно
     * 404 - Событие не найдено или недоступно
     */
    @GetMapping("/events/{id}")
    public EventFullDto getPublishedEventById(@PathVariable @Positive Long id,
                                              HttpServletRequest request) {
        return eventService.getPublishedEventById(id, request);
    }
}

