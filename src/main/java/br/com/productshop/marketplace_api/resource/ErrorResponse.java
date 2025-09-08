package br.com.productshop.marketplace_api.resource;

public record ErrorResponse(
    int status,
    String message
) {}
