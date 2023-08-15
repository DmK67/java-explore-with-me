package ru.practicum.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Long id;
    @Size(min = 1, max = 50)
    @NotNull
    @NotBlank
    private String name;
}
