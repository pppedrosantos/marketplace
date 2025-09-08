package br.com.productshop.marketplace_api.application.exception;

public class InvalidFilterException extends RuntimeException {
    public InvalidFilterException(String message) {
        super("Filtro inválido: " + message);
    }
}
