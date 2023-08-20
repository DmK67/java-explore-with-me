package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * GET /categories Получение категорий. В случае, если по заданным фильтрам не найдено ни одной категории,
     * возвращает пустой список.
     * from - количество категорий, которые нужно пропустить для формирования текущего набора. Default value : 0
     * size - количество категорий в наборе. Default value : 10
     * Responses:
     * 200 - Категории найдены
     * 400 - Запрос составлен некорректно
     */
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }

    /**
     * GET /categories/{catId} Получение информации о категории по её идентификатору. В случае, если категории
     * с заданным id не найдено, возвращает статус код 404.
     * catId - id категории
     * Responses:
     * 200 - Категория найдена
     * 400 - Запрос составлен некорректно
     * 404 - Категория не найдена или недоступна
     */
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Validated NewCategoryDto newCategoryDto) {
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @DeleteMapping(value = "/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }
}
