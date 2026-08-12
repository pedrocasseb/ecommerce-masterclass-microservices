package com.ecommerce.orderservice.exception;

public class ProductServiceUnavailableException extends RuntimeException {

    public ProductServiceUnavailableException(Long productId, Throwable cause) {
        super("Product service unavailable while fetching product: " + productId, cause);
    }
}