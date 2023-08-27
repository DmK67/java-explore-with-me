package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserShortDto {

    private Long id;

    /**
     * Имя
     */
    private String name;
}
