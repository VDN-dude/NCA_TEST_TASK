package com.nca.exception;

import com.nca.dto.response.ExceptionResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * {@code ExceptionsHandler} to handle internal exceptions and send response with necessary message about problem.
 */

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {


    /**
     * Handling {@code EntityNotFoundException}.
     *
     * @param exception
     * @param request
     *
     * @return {@code ResponseEntity} with {@link ExceptionResponseDTO} as response body.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.NOT_FOUND, exception), HttpStatus.NOT_FOUND);
    }

    /**
     * Handling {@code CommentsNotBelongToNewsException}.
     *
     * @param exception
     * @param request
     *
     * @return {@code ResponseEntity} with {@link ExceptionResponseDTO} as response body.
     */
    @ExceptionHandler(CommentsNotBelongToNewsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleCommentsNotBelongToNewsException(
            CommentsNotBelongToNewsException exception, WebRequest request) {
        log.error("Exception occurred: {}, Request Details: {}", exception.getMessage(), request.getDescription(false), exception);
        return new ResponseEntity<>(buildResponse(HttpStatus.FORBIDDEN, exception), HttpStatus.FORBIDDEN);
    }

    /**
     * Building {@code ExceptionResponseDTO}.
     *
     * @param httpStatus
     * @param exception
     *
     * @return {@link ExceptionResponseDTO}.
     */
    private ExceptionResponseDTO buildResponse(HttpStatus httpStatus, Exception exception) {
        return ExceptionResponseDTO.builder()
                .status(httpStatus.value())
                .message(exception.getMessage())
                .build();
    }
}
