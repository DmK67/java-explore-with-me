package ru.practicum.request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResultDto {

    /**
     * Заявки на участие в событии
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /**
     * Заявки на участие в событии
     */
    private List<ParticipationRequestDto> rejectedRequests;
}
