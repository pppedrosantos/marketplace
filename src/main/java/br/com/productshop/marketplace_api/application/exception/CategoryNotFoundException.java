package br.com.productshop.marketplace_api.application.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String category) {
        super("Categoria não encontrada: " + category);
    }
}
