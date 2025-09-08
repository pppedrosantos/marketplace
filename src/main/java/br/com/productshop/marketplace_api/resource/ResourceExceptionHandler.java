package br.com.productshop.marketplace_api.resource;

import br.com.productshop.marketplace_api.application.exception.CategoryNotFoundException;
import br.com.productshop.marketplace_api.application.exception.InvalidFilterException;
import br.com.productshop.marketplace_api.application.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFilter(InvalidFilterException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor"
            ));
    }
}
