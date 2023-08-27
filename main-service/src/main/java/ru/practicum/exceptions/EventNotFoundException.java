package ru.practicum.exceptions;

import javax.persistence.EntityNotFoundException;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(long id) {
        super(String.format("Event with id=%d was not found", id));
    }
}
