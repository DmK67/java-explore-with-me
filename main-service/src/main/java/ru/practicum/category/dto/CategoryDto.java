package ru.practicum.category.dto;

import lombok.*;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

    /**
     * Идентификатор категории
     */
    private Long id;

    /**
     * Название категории
     */
    private String name;
}
