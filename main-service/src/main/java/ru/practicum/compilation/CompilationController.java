package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CompilationController {
    private final CompilationService compilationService;

    /**
     * GET /compilations Получение подборок событий
     * В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список
     * pinned - искать только закрепленные/не закрепленные подборки
     * from - количество элементов, которые нужно пропустить для формирования текущего
     * набора Default value : 0
     * size - количество элементов в наборе Default value : 10
     * Responses:
     * 200 - Найдены подборки событий
     * 400 - Запрос составлен некорректно
     */
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * GET /compilations/{compId} Получение подборки событий по его id. В случае, если подборки с заданным id
     * не найдено, возвращает статус код 404
     * compId - id подборки
     * Responses:
     * 200 - Подборка событий найдена
     * 400 - Запрос составлен некорректно
     * 404 - Подборка не найдена или недоступна
     */
    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Valid @Positive Long compId) {
        return compilationService.getCompilationById(compId);
    }

    /**
     * POST /admin/categories Добавление новой категории. Обратите внимание: имя категории должно быть уникальным
     * Request body - данные добавляемой категории. Example {"name": "Концерты"}
     * Responses:
     * 201 - Категория добавлена
     * 400 - Запрос составлен некорректно
     * 409 - Нарушение целостности данных
     */
    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.createCompilation(newCompilationDto);
    }

    /**
     * PATCH /admin/categories/{catId} Изменение категории. Обратите внимание: имя категории должно быть уникальным
     * catId - id категории
     * Request body - Данные категории для изменения. Example {"name": "Концерты"}
     * Responses:
     * 200 - Данные категории изменены. Example {"id": 1, "name": "Концерты"}
     * 404 - Категория не найдена или недоступна.
     * 409 - Нарушение целостности данных
     */
    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable @Valid @Positive Long compId,
            @RequestBody @Valid UpdateCompilationRequestDto updateCompilationRequestDto) {
        return compilationService.updateCompilation(compId, updateCompilationRequestDto);
    }

    /**
     * DELETE /admin/categories/{catId} Удаление категории. Обратите внимание: с категорией не должно быть связано
     * ни одного события.
     * catId - id категории
     * Responses:
     * 204 - Категория удалена
     * 404 - Категория не найдена или недоступна
     * 409 - Существуют события, связанные с категорией
     */
    @DeleteMapping(value = "/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }
}
