package ru.practicum.request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequestDto {

    /**
     * Идентификаторы запросов на участие в событии текущего пользователя
     */
    private List<Long> requestIds;

    /**
     * Новый статус запроса на участие в событии текущего пользователя
     */
    private String status;
}
