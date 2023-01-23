package ru.practicum.shareit.utils.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.utils.exeptions.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.user.controller.UserController;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {
        UserController.class,
        ItemController.class,
        ItemRequestController.class,
        BookingController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElementException(final NoSuchElementException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(final ConstraintViolationException e) {
        return Map.of("error", e.getMessage());
    }
}
