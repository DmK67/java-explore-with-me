package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserRequestDto {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name; // Имя

    @NotNull
    @Email
    @Size(min = 6, max = 254)
    private String email; // Почтовый адрес(e-mail)
}
