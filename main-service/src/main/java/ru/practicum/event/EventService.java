package ru.practicum.event;

import ru.practicum.event.dto.*;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                        String rangeEnd, int from, int size);

    List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                           String reqUrl, String reqIp);

    EventFullDto getPublishedEventById(Long eventId, String reqUrl, String reqIp);
}
