package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.category.dto.CategoryMapper.toCategory;
import static ru.practicum.category.dto.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Getting a list of categories: from = " + from + ", size = " + size);
        return categoryRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        log.info("Getting information about a category by ID: cat_id = " + catId);
        return toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId)));
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        log.info("Adding a new category: category name = " + newCategoryDto);
        return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategory(long catId, NewCategoryDto newCategoryDto) {
        log.info("Updating category: cat_id = " + catId + ", category name = " + newCategoryDto);
        Category existCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
        Category updatedCategory = toCategory(newCategoryDto);
        updatedCategory.setId(existCategory.getId());
        return toCategoryDto(categoryRepository.save(updatedCategory));
    }

    @Override
    public void deleteCategory(long catId) {
        log.info("Deleting category: cat_id = " + catId);
        categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
        Event event = eventRepository.findFirstByCategoryId(catId);
        if (event != null) {
            throw new ForbiddenException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }
}
