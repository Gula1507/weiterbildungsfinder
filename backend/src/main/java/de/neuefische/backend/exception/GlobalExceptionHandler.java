package de.neuefische.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred. ";
    private static final String SUPPORT_MESSAGE = "Please contact support if the problem persists.";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception exception) {
        logger.error(UNEXPECTED_ERROR_MESSAGE+"{}", exception.getMessage(), exception);
        return new ErrorMessage(UNEXPECTED_ERROR_MESSAGE + SUPPORT_MESSAGE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        logger.warn("Invalid request body:{}", exception.getMessage(), exception);

        return new ErrorMessage("Invalid request body. Please ensure the request contains valid JSON.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationExceptions(MethodArgumentNotValidException exception) {
        logger.warn(UNEXPECTED_ERROR_MESSAGE, exception);
        List<String> errorMessages = exception.getBindingResult()
                                              .getFieldErrors()
                                              .stream()
                                              .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                              .toList();

        String errorMessage = String.join(", ", errorMessages);

        return new ErrorMessage("Validation failed: " + errorMessage);
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleOrganizationNotFoundException(OrganizationNotFoundException exception) {
        logger.warn(UNEXPECTED_ERROR_MESSAGE, exception);
        return new ErrorMessage(exception.getMessage());
    }

}
