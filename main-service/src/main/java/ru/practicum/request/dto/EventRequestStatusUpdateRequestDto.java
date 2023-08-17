package ru.practicum.request.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequestDto {

    private List<Long> requestIds;

    private String status;
}
