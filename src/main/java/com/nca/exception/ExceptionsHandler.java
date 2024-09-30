package com.nca.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.nca.dto.response.ExceptionResponseDTO;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.nca.exception.Message.BAD_PARAMETERS_VALUE;
import static com.nca.exception.Message.UNKNOWN_PROPERTY;

/**
 * {@code ExceptionsHandler} to handle internal exceptions and send response with necessary message about problem.
 */

@Slf4j
@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        if (exception.getRootCause().getClass().equals(UnrecognizedPropertyException.class)) {
            UnrecognizedPropertyException rootCause = (UnrecognizedPropertyException) exception.getRootCause();

            ExceptionResponseDTO exceptionResponseDTO = ExceptionResponseDTO.builder()
                    .status(status.value())
                    .message(String.format(UNKNOWN_PROPERTY.getMessage(),
                            rootCause.getPropertyName(),
                            rootCause.getKnownPropertyIds()))
                    .build();

            log.error("Exception occurred: {}, Request Details: {}",
                    exception.getMessage(), request.getDescription(false), exception);
            return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, exception), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, String> errMap = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errMap.put(error.getField(), error.getDefaultMessage()));

        ExceptionResponseDTO exceptionResponseDTO = ExceptionResponseDTO.builder()
                .status(status.value())
                .message(String.format(BAD_PARAMETERS_VALUE.getMessage(), errMap))
                .build();

        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionResponseDTO> handleJwtException(
            JwtException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.BAD_REQUEST, exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthenticationException(
            AuthenticationException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.UNAUTHORIZED, exception), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccessDeniedException(
            AccessDeniedException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.FORBIDDEN, exception), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChangeResourceAccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleChangeResourceAccessDeniedException(
            ChangeResourceAccessDeniedException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.FORBIDDEN, exception), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.CONFLICT, exception), HttpStatus.CONFLICT);
    }

    /**
     * Handling {@code EntityNotFoundException}.
     *
     * @param exception
     * @param request
     * @return {@code ResponseEntity} with {@link ExceptionResponseDTO} as response body.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.NOT_FOUND, exception), HttpStatus.NOT_FOUND);
    }

    /**
     * Handling {@code CommentsNotBelongToNewsException}.
     *
     * @param exception
     * @param request
     * @return {@code ResponseEntity} with {@link ExceptionResponseDTO} as response body.
     */
    @ExceptionHandler(CommentsNotBelongToNewsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleCommentsNotBelongToNewsException(
            CommentsNotBelongToNewsException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}",
                exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.FORBIDDEN, exception), HttpStatus.FORBIDDEN);
    }

    /**
     * Building {@code ExceptionResponseDTO}.
     *
     * @param httpStatus
     * @param exception
     * @return {@link ExceptionResponseDTO}.
     */
    private ExceptionResponseDTO buildResponse(HttpStatus httpStatus, Exception exception) {
        return ExceptionResponseDTO.builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .build();
    }
}
