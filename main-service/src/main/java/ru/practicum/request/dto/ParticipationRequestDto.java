package ru.practicum.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    private Long event;

    private Long requester;

    private String status;

    private String created;
}
