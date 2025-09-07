package br.com.productshop.marketplace_api.application.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Produto não encontrado com ID: " + id);
    }
}
