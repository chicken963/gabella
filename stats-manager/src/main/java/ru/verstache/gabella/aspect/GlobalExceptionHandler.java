package ru.verstache.gabella.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.verstache.gabella.exception.ItemNotFoundException;
import ru.verstache.gabella.exception.ReportListSizeException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleException(ItemNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found: " + ex.getMessage());
    }

    @ExceptionHandler(ReportListSizeException.class)
    public ResponseEntity<String> handleInvalidAmountException(ReportListSizeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
