package root.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleObjectNotFoundException(ObjectNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", e.getMessage());
        return errors;
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleIOException(ImageNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error", e.getMessage());
        return errors;
    }
}
