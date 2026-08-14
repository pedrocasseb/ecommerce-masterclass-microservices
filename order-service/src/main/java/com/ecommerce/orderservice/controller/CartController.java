package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.model.CartItem;
import com.ecommerce.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request
    ) {
        log.info("Adding product to cart - userId: {}, productId: {}, quantity: {}",
                userId, request.getProductId(), request.getQuantity());

        if (!cartService.addToCart(userId, request)) {
            log.warn("Failed to add product to cart - userId: {}, productId: {}. " +
                            "Product out of stock, user not found or product not found",
                    userId, request.getProductId());

            return ResponseEntity.badRequest()
                    .body("Product out of stock or user not found or product not found");
        }

        log.info("Product successfully added to cart - userId: {}, productId: {}",
                userId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId
    ) {
        log.info("Removing product from cart - userId: {}, productId: {}",
                userId, productId);

        boolean deleted = cartService.deleteItemFromCart(userId, productId);

        if (!deleted) {
            log.warn("Product not found in cart - userId: {}, productId: {}",
                    userId, productId);

            return ResponseEntity.notFound().build();
        }

        log.info("Product successfully removed from cart - userId: {}, productId: {}",
                userId, productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId
    ) {
        log.info("Fetching cart - userId: {}", userId);

        List<CartItem> cart = cartService.getCart(userId);

        log.info("Cart successfully fetched - userId: {}, items: {}",
                userId, cart.size());

        return ResponseEntity.ok(cart);
    }
}
