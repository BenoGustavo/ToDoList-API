package dev.gustavo.ToDoListAPI.utils.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.DtoConvertionHandler;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("null")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ex.getMethod() + " method is not supported for this endpoint, expected methods are: "
                        + ex.getSupportedHttpMethods());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMostSpecificCause().getMessage().split("Detail: ")[0]);
    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Object> handleDatatypeConverter(ClassNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex);
    }

    @ExceptionHandler(DtoConvertionHandler.class)
    public ResponseEntity<Response<DtoConvertionHandler>> handleDtoConvertion(DtoConvertionHandler ex) {
        Response<DtoConvertionHandler> responseBody = new ResponseBuilder<DtoConvertionHandler>()
                .error(ex.getCode(), ex.getMessage())
                .status(ex.getCode()).result("Invalid payload, check the docs!").build();

        return ResponseEntity.status(ex.getCode()).body(responseBody);
    }

    @ExceptionHandler(BadRequest400Exception.class)
    public ResponseEntity<Response<BadRequest400Exception>> handleBadRequestException(BadRequest400Exception ex) {
        Response<BadRequest400Exception> responseBody = new ResponseBuilder<BadRequest400Exception>()
                .result("Bad Request!").error(ex.getCode(), ex.getMessage()).status(ex.getCode()).build();

        return ResponseEntity.status(ex.getCode()).body(responseBody);
    }

    @ExceptionHandler(Unauthorized401Exception.class)
    public ResponseEntity<Response<Unauthorized401Exception>> handleUnauthorizedRequests(Unauthorized401Exception ex) {
        Response<Unauthorized401Exception> responseBody = new ResponseBuilder<Unauthorized401Exception>()
                .result("Unauthorized").error(ex.getCode(), ex.getMessage()).status(ex.getCode()).build();

        return ResponseEntity.status(ex.getCode()).body(responseBody);
    }

    @ExceptionHandler(NotFound404Exception.class)
    public ResponseEntity<Response<NotFound404Exception>> handleNotFoundRequests(NotFound404Exception ex) {
        Response<NotFound404Exception> responseBody = new ResponseBuilder<NotFound404Exception>()
                .result("Not Found").error(ex.getCode(), ex.getMessage()).status(ex.getCode()).build();

        return ResponseEntity.status(ex.getCode()).body(responseBody);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<HttpMessageNotReadableException>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        String message = ex.getMessage().split(":")[0];

        Response<HttpMessageNotReadableException> responseBody = new ResponseBuilder<HttpMessageNotReadableException>()
                .result("empty payload, check the docs!").error(400, message).status(400).build();

        return ResponseEntity.status(400).body(responseBody);
    }
}