package ru.practicum.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id; // Идентификатор заявки

    private Long event; // Идентификатор события

    private Long requester; // Идентификатор пользователя, отправившего заявку

    private String status; // Статус заявки

    private String created; // Дата и время создания заявки
}
