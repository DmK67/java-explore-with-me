package ru.practicum.exceptions;

import javax.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(long id) {
        super(String.format("Category with id=%d was not found", id));
    }
}
