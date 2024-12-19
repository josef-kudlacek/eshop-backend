package cz.jkdabing.backend.exception.handler;

import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.exception.custom.ImageAlreadyExistsException;
import cz.jkdabing.backend.exception.custom.NotFoundException;
import cz.jkdabing.backend.exception.custom.UserAlreadyExistsException;
import cz.jkdabing.backend.exception.dto.ErrorMessageResponse;
import cz.jkdabing.backend.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private final MessageService messageService;

    public ControllerExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler({BadRequestException.class, ImageAlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageResponse> handleBadRequestExceptions(Exception exception, WebRequest webRequest) {
        logger.warn("Bad request exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessageResponse);
    }

    @ExceptionHandler({NotFoundException.class, FileNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundExceptions(Exception exception, WebRequest webRequest) {
        logger.info("Resource not found exception: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            WebRequest webRequest
    ) {
        logger.warn("Bad request exception occurred: ", exception);

        Map<String, List<String>> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.computeIfAbsent(fieldName, errorMessages -> new ArrayList<>())
                    .add(errorMessage);
        });

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                messageService.getMessage("error.validation.failed"),
                errors,
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessageResponse> handleAccessDeniedException(
            AuthorizationDeniedException exception,
            WebRequest webRequest
    ) {
        logger.error("Unauthorized exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception,
            WebRequest webRequest
    ) {
        logger.warn("User already exists exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.CONFLICT.value(),
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> globalExceptionHandler(Exception exception, WebRequest webRequest) {
        logger.error("Exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                messageService.getMessage("error.server.side"),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessageResponse> handleIOException(Exception exception, WebRequest webRequest) {
        logger.error("IO Exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                messageService.getMessage("error.io.exception"),
                webRequest.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageResponse> handleMultipartException(Exception exception, WebRequest webRequest) {
        logger.warn("Multipart exception occurred: ", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                messageService.getMessage("error.image.empty.image"),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessageResponse);
    }
}
