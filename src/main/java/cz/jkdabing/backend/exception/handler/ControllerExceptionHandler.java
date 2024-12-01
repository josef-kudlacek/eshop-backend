package cz.jkdabing.backend.exception.handler;

import cz.jkdabing.backend.exception.custom.*;
import cz.jkdabing.backend.exception.dto.ErrorMessageResponse;
import cz.jkdabing.backend.service.MessageService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

    @ExceptionHandler({
            BadRequestException.class, ImageAlreadyExistsException.class, ExampleAlreadyExistsException.class,
            FileNameAlreadyExistsException.class
    })
    public ResponseEntity<ErrorMessageResponse> handleBadRequestExceptions(Exception exception, WebRequest webRequest) {
        logger.warn("Bad request exception occurred: ", exception);

        HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusBadRequest.value(), exception.getMessage(), webRequest
        );

        return ResponseEntity.status(httpStatusBadRequest)
                .body(errorMessageResponse);
    }

    @ExceptionHandler({
            NotFoundException.class, FileNotFoundException.class, EntityNotFoundException.class,
            AudioFileNotExistException.class
    })
    public ResponseEntity<ErrorMessageResponse> handleNotFoundExceptions(Exception exception, WebRequest webRequest) {
        logger.info("Resource not found exception: ", exception);

        HttpStatus httpStatusNotFound = HttpStatus.NOT_FOUND;
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusNotFound.value(), exception.getMessage(), webRequest
        );

        return ResponseEntity.status(httpStatusNotFound)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
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

        HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;
        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                httpStatusBadRequest.value(),
                new Date(),
                messageService.getMessage("error.validation.failed"),
                errors,
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(httpStatusBadRequest)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessageResponse> handleAccessDeniedException(
            AuthorizationDeniedException exception,
            WebRequest webRequest
    ) {
        logger.error("Unauthorized exception occurred: ", exception);

        HttpStatus httpStatusForbidden = HttpStatus.FORBIDDEN;
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusForbidden.value(), exception.getMessage(), webRequest
        );

        return ResponseEntity.status(httpStatusForbidden)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception,
            WebRequest webRequest
    ) {
        logger.warn("User already exists exception occurred: ", exception);

        HttpStatus httpStatusConflict = HttpStatus.CONFLICT;
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusConflict.value(), exception.getMessage(), webRequest
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorMessageResponse);
    }

    @ExceptionHandler({Exception.class, InvalidFileExtensionException.class})
    public ResponseEntity<ErrorMessageResponse> globalExceptionHandler(Exception exception, WebRequest webRequest) {
        logger.error("Exception occurred: ", exception);

        HttpStatus httpStatusInternalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        String errorMessage = messageService.getMessage("error.server.side");
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusInternalServerError.value(), errorMessage, webRequest
        );

        return ResponseEntity.status(httpStatusInternalServerError)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorMessageResponse> handleIOException(Exception exception, WebRequest webRequest) {
        logger.error("IO Exception occurred: ", exception);

        HttpStatus httpStatusInternalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        String errorMessage = messageService.getMessage("error.io.exception");
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusInternalServerError.value(), errorMessage, webRequest
        );

        return ResponseEntity.status(httpStatusInternalServerError)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorMessageResponse> handleImageMultipartException(Exception exception, WebRequest webRequest) {
        logger.warn("Multipart exception occurred: ", exception);

        HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;

        String errorMessage = messageService.getMessage("error.image.empty.image");
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusBadRequest.value(), errorMessage, webRequest
        );

        return ResponseEntity.status(httpStatusBadRequest)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleAudioFileMultipartException(Exception exception, WebRequest webRequest) {
        logger.warn("Multipart exception occurred: ", exception);

        HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;

        String errorMessage = messageService.getMessage("error.audio.file.empty");
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusBadRequest.value(), errorMessage, webRequest
        );

        return ResponseEntity.status(httpStatusBadRequest)
                .body(errorMessageResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorMessageResponse> handleMaximumUploadSizeExceeded(Exception exception, WebRequest webRequest) {
        logger.warn("Multipart maximum upload size exceeded: ", exception);

        HttpStatus httpStatusBadRequest = HttpStatus.BAD_REQUEST;

        String errorMessage = messageService.getMessage("error.file.size.exceed.limit");
        ErrorMessageResponse errorMessageResponse = getErrorMessageResponse(
                httpStatusBadRequest.value(), errorMessage, webRequest
        );

        return ResponseEntity.status(httpStatusBadRequest)
                .body(errorMessageResponse);
    }

    private ErrorMessageResponse getErrorMessageResponse(
            int httpStatusCode,
            String errorMessage,
            WebRequest webRequest
    ) {
        return new ErrorMessageResponse(
                httpStatusCode, new Date(), errorMessage, webRequest.getDescription(false)
        );
    }
}
