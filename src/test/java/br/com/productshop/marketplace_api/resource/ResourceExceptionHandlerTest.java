package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceExceptionHandlerTest {

    private final ResourceExceptionHandler exceptionHandler = new ResourceExceptionHandler();

    @Test
    void shouldHandleProductNotFoundException() {
        // Arrange
        String productId = "123";
        ProductNotFoundException exception = new ProductNotFoundException(productId);
        String expectedMessage = "Produto não encontrado com ID: " + productId;

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleProductNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals(expectedMessage, response.getBody().message());
    }

    @Test
    void shouldHandleGenericExceptionWithMessage() {
        // Arrange
        String errorMessage = "Erro específico";
        Exception exception = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleGenericExceptionWithNullMessage() {
        // Arrange
        Exception exception = new RuntimeException();

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals("Erro interno do servidor", response.getBody().message());
    }
}
