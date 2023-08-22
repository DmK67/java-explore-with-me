package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    /**
     * Название категории
     */
    @Size(min = 1, max = 50)
    @NotBlank
    private String name;
}
