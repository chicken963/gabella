package ru.verstache.gabella.aspect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.verstache.gabella.exception.ItemNotFoundException;
import ru.verstache.gabella.exception.ReportListSizeException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler uut;

    @Test
    void shouldHandleItemNotFoundException() {
        ResponseEntity<String> itemNotFound = uut.handleException(new ItemNotFoundException("Item not found"));
        assertThat(itemNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldHandleInvalidAmountException() {
        ResponseEntity<String> itemNotFound = uut.handleInvalidAmountException(new ReportListSizeException("Wrong path variable value"));
        assertThat(itemNotFound.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
