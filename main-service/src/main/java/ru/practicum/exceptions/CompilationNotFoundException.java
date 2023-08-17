package ru.practicum.exceptions;

import javax.persistence.EntityNotFoundException;

public class CompilationNotFoundException extends EntityNotFoundException {
    public CompilationNotFoundException(Long id) {
        super(String.format("Compilation with id=%d was not found", id));
    }
}
