package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.NewUserRequestDto;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {
    private final UserService userService;

    /**
     * GET /admin/users Получение информации о пользователях. Возвращает информацию обо всех пользователях
     * (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы). В случае,
     * если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список.
     * ids - id пользователей
     * from - количество элементов, которые нужно пропустить для формирования текущего набора. Default value : 0
     * size - количество элементов в наборе. Default value : 10
     * Responses:
     * 200 - Пользователи найдены
     * 400 - Запрос составлен некорректно
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return ids == null ? userService.getUsers(from, size) : userService.getUsers(ids, from, size);
    }

    /**
     * POST /admin/users Добавление нового пользователя
     * Request body - Данные добавляемого пользователя
     * Responses:
     * 201 - Пользователь зарегистрирован
     * 400 - Запрос составлен некорректно
     * 409 - Нарушение целостности данных
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated NewUserRequestDto newUserRequestDto) {
        return userService.createUser(newUserRequestDto);
    }

    /**
     * DELETE /admin/users/{userId} Удаление пользователя
     * userId - id пользователя
     * Responses:
     * 204 - Пользователь удален
     * 404 - Пользователь не найден или недоступен
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
