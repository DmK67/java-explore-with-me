package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventState;
import ru.practicum.exceptions.EventNotFoundException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.ParticipationRequestMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.request.dto.ParticipationRequestMapper.toParticipationRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        log.info("Adding a request from the current user to participate in the event: user_id = " + userId
                + ", event_id = " + eventId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        ParticipationRequest existParticipationRequest = participationRequestRepository
                .findByRequesterIdAndEventId(userId, eventId);
        if (existParticipationRequest != null) {
            log.info("Error: User with ID = " + userId + " can't add a repeat request with ID = " + eventId);
            throw new ForbiddenException("Could not add the same request.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            log.info("Error: initiator with ID = " + userId + " can't add a request to participate in his event" +
                    " with ID = " + eventId);
            throw new ForbiddenException("Initiator could not add request to own event.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            log.info("Error: User with ID = " + userId + " cannot participate in an unpublished event with an" +
                    " ID = " + eventId);
            throw new ForbiddenException("Could not participate in non-published event.");
        }
        if (event.getParticipantLimit() != 0 && participationRequestRepository.countByEventIdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            log.info("Error: User with ID = " + userId + " cannot participate in an event with an ID = " + eventId
                    + ", since the limit of participation requests has been reached");
            throw new ForbiddenException("Reach participant limit.");
        }
        ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            status = ParticipationRequestStatus.CONFIRMED;
        }
        ParticipationRequest newParticipationRequest = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .status(status)
                .build();
        return toParticipationRequestDto(participationRequestRepository.save(newParticipationRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        log.info("Cancellation of your request to participate in the event: user_id = " + userId + ", request_id = "
                + requestId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ParticipationRequest requestToUpdate = participationRequestRepository.getReferenceById(requestId);
        requestToUpdate.setStatus(ParticipationRequestStatus.CANCELED);
        return toParticipationRequestDto(participationRequestRepository.save(requestToUpdate));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        log.info("Getting information about the current user's requests to participate in other people's events:" +
                " user_id = " + userId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Optional<ParticipationRequest>> requests = participationRequestRepository.findByRequesterId(userId);
        return requests.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsForUserEvent(Long userId, Long eventId) {
        log.info("Getting information about requests to participate in the event of the current user: user_id = "
                + userId + ", event_id = " + eventId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Event> userEvents = eventRepository.findByIdAndInitiatorId(eventId, userId);
        List<Optional<ParticipationRequest>> requests = participationRequestRepository.findByEventIn(userEvents);
        return requests.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto changeParticipationRequestsStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequest) {
        log.info("Changing the status (confirmed, canceled) of applications for participation in the event of the" +
                " current user: user_id = " + userId + ", event_id = " + eventId + ", новый статус = "
                + eventRequestStatusUpdateRequest);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<ParticipationRequest> requests = participationRequestRepository
                .findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto
                = EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();
        if (!requests.isEmpty()) {
            if (ParticipationRequestStatus.valueOf(eventRequestStatusUpdateRequest.getStatus())
                    == ParticipationRequestStatus.CONFIRMED) {
                int limitParticipants = event.getParticipantLimit();
                if (limitParticipants == 0 || !event.isRequestModeration()) {
                    throw new ForbiddenException("Do not need accept requests, cause participant limit equals 0 or " +
                            "pre-moderation off");
                }
                Integer countParticipants = participationRequestRepository.countByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED);
                if (countParticipants == limitParticipants) {
                    throw new ForbiddenException("The participant limit has been reached");
                }
                for (ParticipationRequest request : requests) {
                    if (request.getStatus() != ParticipationRequestStatus.PENDING) {
                        throw new ForbiddenException("Status of request doesn't PENDING");
                    }
                    if (countParticipants < limitParticipants) {
                        request.setStatus(ParticipationRequestStatus.CONFIRMED);
                        eventRequestStatusUpdateResultDto.getConfirmedRequests()
                                .add(toParticipationRequestDto(request));
                        countParticipants++;
                    } else {
                        request.setStatus(ParticipationRequestStatus.REJECTED);
                        eventRequestStatusUpdateResultDto.getRejectedRequests()
                                .add(toParticipationRequestDto(request));
                    }
                }
                participationRequestRepository.saveAll(requests);
                if (countParticipants == limitParticipants) {
                    participationRequestRepository.updateRequestStatusByEventIdAndStatus(event,
                            ParticipationRequestStatus.PENDING, ParticipationRequestStatus.REJECTED);
                }
            } else if (ParticipationRequestStatus.valueOf(eventRequestStatusUpdateRequest.getStatus())
                    == ParticipationRequestStatus.REJECTED) {
                for (ParticipationRequest request : requests) {
                    if (request.getStatus() != ParticipationRequestStatus.PENDING) {
                        throw new ForbiddenException("Status of request doesn't PENDING");
                    }
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    eventRequestStatusUpdateResultDto.getRejectedRequests().add(toParticipationRequestDto(request));
                }
                participationRequestRepository.saveAll(requests);
            }
        }
        return eventRequestStatusUpdateResultDto;
    }
}
