package ru.practicum.exceptions;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public CommentNotFoundException(Long id) {
        super(String.format("Comment with id=%d was not found", id));
    }
}
