package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.StatsClient;
import ru.practicum.StatsDto;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.exceptions.*;
import ru.practicum.location.Location;
import ru.practicum.location.LocationRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.event.dto.EventMapper.*;
import static ru.practicum.location.dto.LocationMapper.toLocation;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        log.info("Adding a new event: user_id = " + userId + ", event = " + newEventDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory()));
        Event event = toEvent(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException("Cannot create the event, because less 2 hours before event datetime");
        }
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setState(EventState.PENDING);
        event.setLocation(locationRepository.save(toLocation(newEventDto.getLocation())));
        event.setViews(0L);
        return toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        log.info("Getting events added by the current user: user_id = " + userId + ", from = " + from +
                ", size = " + size);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        log.info("Getting full information about the event added by the current user: user_id = " + userId +
                ", event_id = " + eventId);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return toEventFullDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId)));
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId,
                                          UpdateEventUserRequestDto updateEventUserRequestDto) {
        log.info("Updating event information: user_id = " + userId + ", event_id = " + eventId +
                ", update_event = " + updateEventUserRequestDto);
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getState() != null && event.getState() != EventState.PENDING && event.getState()
                != EventState.CANCELED) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequestDto.getEventDate() != null
                && LocalDateTime.parse(updateEventUserRequestDto.getEventDate(), formatter)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException(String.format("Event date must not be before 2 hours from current" +
                            " time. New value: %s",
                    updateEventUserRequestDto.getEventDate()));
        }
        return updateEventUser(event, updateEventUserRequestDto);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        log.info("updating information about the event by the administrator: event_id = " + eventId
                + ", update_event = " + updateEventAdminRequestDto);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (updateEventAdminRequestDto.getStateAction() != null) {
            if (updateEventAdminRequestDto.getStateAction() == StateAdminAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    throw new ForbiddenException("Cannot publish the event because it's not in the right state: "
                            + event.getState());
                }
                if (event.getPublishedOn()
                        != null && event.getEventDate().isAfter(event.getPublishedOn().minusHours(1))) {
                    throw new ValidationRequestException("Cannot publish the event because it's after 1 hour" +
                            " before event datetime");
                }
                event.setPublishedOn(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            }
            if (updateEventAdminRequestDto.getStateAction() == StateAdminAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new ForbiddenException("Cannot reject the event because it's already published");
                } else {
                    event.setState(EventState.CANCELED);
                }
            }
        }
        if (updateEventAdminRequestDto.getEventDate() != null
                && LocalDateTime.parse(updateEventAdminRequestDto.getEventDate(), formatter)
                .isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationRequestException(String.format("Event date must not be before 2 hours from current" +
                            " time. New value: %s",
                    updateEventAdminRequestDto.getEventDate()));
        }
        return updateEventAdmin(event, updateEventAdminRequestDto);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        log.info("Search for events by parameters: user_ids = " + users + ", states = " + states
                + ", categories = " + categories + ", rangeStart = " + rangeStart + ", rangeEnd = " + rangeEnd);
        validateEventStates(states);
        List<Event> events = eventRepository.findEvents(users,
                states, categories,
                rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null,
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null,
                PageRequest.of(from / size, size));
        return events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, boolean onlyAvailable, String sort, int from,
                                                  int size, String reqUrl, String reqIp) {
        log.info("Search for published events by parameters: text = " + text + ", categories = " + categories +
                ", paid = " + paid + ", rangeStart = " + rangeStart + ", rangeEnd = " + rangeEnd +
                ", onlyAvailable = " + onlyAvailable + ", sort = " + sort + ", from = " + from +
                ", size = " + size);
        log.info("Client ip: {}", reqIp);
        log.info("Endpoint path: {}", reqUrl);
        statsClient.addHit(HitDto.builder()
                .app("ewm-main-service")
                .uri(reqUrl)
                .ip(reqIp)
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        if (rangeStart != null && rangeEnd != null &&
                LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter))) {
            throw new ValidationRequestException("Date start is after date end.");
        }
        List<Event> events = eventRepository.findPublishedEvents(
                text,
                categories,
                paid,
                rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : LocalDateTime.now(),
                rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null,
                PageRequest.of(from / size, size));
        List<EventShortDto> eventShortDtos = Collections.emptyList();
        if (events != null) {
            eventShortDtos = events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
            if (onlyAvailable) {
                eventShortDtos = events.stream().filter(event ->
                        countConfirmedRequests(event.getRequests()) < event.getParticipantLimit()
                ).map(EventMapper::toEventShortDto).collect(Collectors.toList());
            }
            if (sort != null) {
                switch (EventSort.valueOf(sort)) {
                    case EVENT_DATE:
                        eventShortDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
                        break;
                    case VIEWS:
                        eventShortDtos.sort(Comparator.comparing(EventShortDto::getViews));
                        break;
                    default:
                        throw new ValidationRequestException("Parameter sort is not valid");
                }
            }
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request) {
        log.info("Getting information about a published event by ID: event_id = " + eventId);
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        event.setViews(0L);
        statsClient.addHit(HitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build());
        event.setViews(Long.valueOf(getCountHits(request)));
        return toEventFullDto(event);
    }

    private EventFullDto updateEventAdmin(Event event, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }
        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }
        if (updateEventAdminRequestDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequestDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(updateEventAdminRequestDto.getCategory())));
        }
        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }
        if (updateEventAdminRequestDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventAdminRequestDto.getEventDate(), formatter));
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(updateEventAdminRequestDto.getLocation().getLat());
            location.setLon(updateEventAdminRequestDto.getLocation().getLon());
        }
        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        return toEventFullDto(eventRepository.save(event));
    }

    private EventFullDto updateEventUser(Event event, UpdateEventUserRequestDto updateEventUserRequestDto) {
        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }
        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }
        if (updateEventUserRequestDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventUserRequestDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(updateEventUserRequestDto.getCategory())));
        }
        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }
        if (updateEventUserRequestDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateEventUserRequestDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(updateEventUserRequestDto.getLocation().getLat());
            location.setLon(updateEventUserRequestDto.getLocation().getLon());
        }
        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getStateAction() == null) {
            return toEventFullDto(eventRepository.save(event));
        }
        if (updateEventUserRequestDto.getStateAction() == StateUserAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventUserRequestDto.getStateAction() == StateUserAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
        return toEventFullDto(eventRepository.save(event));
    }

    private void validateEventStates(List<String> states) {
        if (states != null) {
            for (String state : states)
                try {
                    EventState.valueOf(state);
                } catch (IllegalArgumentException e) {
                    throw new ValidationRequestException("Wrong states!");
                }
        }
    }

    private Integer getCountHits(HttpServletRequest request) {
        log.info("Client ip: {}", request.getRemoteAddr());
        log.info("Endpoint path: {}", request.getRequestURI());

        ResponseEntity<StatsDto[]> response = statsClient.getStats(
                LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().format(formatter),
                new String[]{request.getRequestURI()},
                true);

        Optional<StatsDto> statDto;
        Integer hits = 0;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            statDto = Arrays.stream(response.getBody()).findFirst();
            if (statDto.isPresent()) {
                hits = statDto.get().getHits();
            }
        }
        return hits;
    }

}
