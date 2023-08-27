package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserRequestDto {

    /**
     * Имя
     */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    /**
     * Почтовый адрес(e-mail)
     */
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
}
