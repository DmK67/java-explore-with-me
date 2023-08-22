package ru.practicum.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    /**
     * Идентификатор заявки
     */
    private Long id;

    /**
     * Идентификатор события
     */
    private Long event;

    /**
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requester;

    /**
     * Статус заявки
     */
    private String status;

    /**
     * Дата и время создания заявки
     */
    private String created;
}
