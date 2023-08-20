package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id; // Идентификатор

    private String name; // Имя

    private String email; // Почтовый адрес(e-mail)
}
