package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.CategoryController;
import ru.practicum.compilation.CompilationController;
import ru.practicum.event.EventController;
import ru.practicum.request.ParticipationRequestController;
import ru.practicum.user.UserController;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice(assignableTypes = {
        UserController.class,
        CategoryController.class,
        EventController.class,
        ParticipationRequestController.class,
        CompilationController.class})
public class ErrorHandler {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException e) {
        return new ApiError("BAD_REQUEST", "Incorrectly made request.",
                e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationRequestException e) {
        return new ApiError("BAD_REQUEST", "Incorrectly made request.",
                e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            CategoryNotFoundException.class,
            EventNotFoundException.class,
            RequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleObjectNotFoundException(EntityNotFoundException e) {
        return new ApiError("NOT_FOUND", "The required object was not found.",
                e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(ConstraintViolationException e) {
        return new ApiError("CONFLICT", "Integrity constraint has been violated.",
                e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleForbiddenException(ForbiddenException e) {
        return new ApiError("FORBIDDEN", "For the requested operation the conditions are not met.",
                e.getMessage(), LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    }
}
