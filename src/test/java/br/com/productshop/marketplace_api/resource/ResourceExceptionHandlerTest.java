package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.exception.CategoryNotFoundException;
import br.com.productshop.marketplace_api.application.exception.InvalidFilterException;
import br.com.productshop.marketplace_api.application.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResourceExceptionHandlerTest {

    private final ResourceExceptionHandler handler = new ResourceExceptionHandler();

    @Test
    void shouldHandleProductNotFoundException() {
        // Arrange
        String productId = "123";
        ProductNotFoundException exception = new ProductNotFoundException(productId);
        String expectedMessage = "Produto não encontrado com ID: " + productId;

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleProductNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals(expectedMessage, response.getBody().message());
    }

    @Test
    void shouldHandleCategoryNotFoundException() {
        // Arrange
        String categoryName = "Eletrônicos";
        CategoryNotFoundException exception = new CategoryNotFoundException(categoryName);
        String expectedMessage = "Categoria não encontrada: " + categoryName;

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleCategoryNotFound(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
        assertEquals(expectedMessage, response.getBody().message());
    }

    @Test
    void shouldHandleInvalidFilterException() {
        // Arrange
        String errorMessage = "Preço mínimo não pode ser negativo";
        InvalidFilterException exception = new InvalidFilterException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleInvalidFilter(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "ID do produto não pode ser vazio";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals(errorMessage, response.getBody().message());
    }

    @Test
    void shouldHandleGenericException() {
        // Arrange
        Exception exception = new RuntimeException("Erro qualquer");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals("Erro interno do servidor", response.getBody().message());
    }
}
