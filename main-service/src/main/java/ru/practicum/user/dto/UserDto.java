package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * Имя
     */
    private String name;

    /**
     * Почтовый адрес(e-mail)
     */
    private String email;
}
