package ru.practicum.request;

import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    List<ParticipationRequestDto> getParticipationRequestsForUserEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto changeParticipationRequestsStatus(Long userId, Long eventId,
                                                                        EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequest);
}
