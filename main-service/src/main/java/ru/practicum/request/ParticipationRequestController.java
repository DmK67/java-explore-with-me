package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    /**
     * POST /users/{userId}/requests Добавление запроса от текущего пользователя на участие в событии.
     * Обратите внимание:
     * нельзя добавить повторный запрос (Ожидается код ошибки 409);
     * инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409);
     * нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409);
     * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409);
     * если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние
     * подтвержденного.
     * userId - id текущего пользователя
     * eventId - id события
     * Responses:
     * 201 - Заявка создана
     * 400 - Запрос составлен некорректно
     * 404 - Событие не найдено или недоступно
     * 409 - Нарушение целостности данных
     */
    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable @Valid @Positive Long userId,
                                                              @RequestParam @Valid @Positive Long eventId) {
        return participationRequestService.createParticipationRequest(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/requests/{requestId}/cancel Отмена своего запроса на участие в событии
     * userId - id текущего пользователя
     * requestId - id запроса на участие
     * Responses:
     * 200 - Заявка отменена
     * 404 - Запрос не найден или недоступен
     */
    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable @Valid @Positive Long userId,
                                                              @PathVariable @Valid @Positive Long requestId) {
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }

    /**
     * GET /users/{userId}/requests Получение информации о заявках текущего пользователя на участие в чужих событиях
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.
     * userId - id текущего пользователя
     * Responses:
     * 200 - Найдены запросы на участие
     * 400 - Запрос составлен некорректно
     * 404 - Пользователь не найден
     */
    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable @Valid @Positive Long userId) {
        return participationRequestService.getParticipationRequests(userId);
    }

    /**
     * GET /users/{userId}/events/{eventId}/requests Получение информации о запросах на участие в событии текущего
     * пользователя. В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список.
     * userId - id текущего пользователя
     * eventId - id события
     * Responses
     * 200 - Найдены запросы на участие
     * 400 - Запрос составлен некорректно
     */
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsForUserEvent(
            @PathVariable @Valid @Positive Long userId,
            @PathVariable @Valid @Positive Long eventId) {
        return participationRequestService.getParticipationRequestsForUserEvent(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/events/{eventId}/requests Изменение статуса (подтверждена, отменена) заявок на участие
     * в событии текущего пользователя. Обратите внимание: если для события лимит заявок равен 0 или отключена
     * пре-модерация заявок, то подтверждение заявок не требуется;
     * нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409);
     * статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409);
     * если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки
     * необходимо отклонить.
     * userId - id текущего пользователя
     * eventId - id события текущего пользователя
     * Request body - Новый статус для заявок на участие в событии текущего пользователя
     * Responses:
     * 200 - Статус заявок изменён
     * 400 - Запрос составлен некорректно
     * 404 - Событие не найдено или недоступно
     * 409 - Достигнут лимит одобренных заявок
     */
    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeParticipationRequestsStatus(
            @PathVariable @Valid @Positive Long userId,
            @PathVariable @Valid @Positive Long eventId,
            @RequestBody EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequest) {
        return participationRequestService.changeParticipationRequestsStatus(userId, eventId,
                eventRequestStatusUpdateRequest);
    }
}
