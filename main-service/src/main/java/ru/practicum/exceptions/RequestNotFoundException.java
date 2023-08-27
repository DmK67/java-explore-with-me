package ru.practicum.exceptions;

import javax.persistence.EntityNotFoundException;

public class RequestNotFoundException extends EntityNotFoundException {

    public RequestNotFoundException(Long id) {
        super(String.format("Request with id=%d was not found", id));
    }
}
