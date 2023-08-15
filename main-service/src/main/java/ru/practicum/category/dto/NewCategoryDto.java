package ru.practicum.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
public class NewCategoryDto {
    @Size(min = 1, max = 50)
    @NotNull
    @NotBlank
    private String name;
}
