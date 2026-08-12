package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.clients.ProductServiceClient;
import com.ecommerce.orderservice.dto.CartItemRequest;
import com.ecommerce.orderservice.dto.ProductResponse;
import com.ecommerce.orderservice.exception.ProductNotFoundException;
import com.ecommerce.orderservice.exception.ProductServiceUnavailableException;
import com.ecommerce.orderservice.model.CartItem;
import com.ecommerce.orderservice.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;

    public boolean addToCart(String userId, CartItemRequest request) {
        ProductResponse productResponse;
        try {
            productResponse = productServiceClient.getProductDetails(Long.valueOf(request.getProductId()));
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException(Long.valueOf(request.getProductId()));
        } catch (HttpServerErrorException e) {
            throw new ProductServiceUnavailableException(Long.valueOf(request.getProductId()), e);
        }

        if (productResponse == null){
            return false;
        }

        if(productResponse.getStockQuantity() < request.getQuantity()) {
            return false;
        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()) {
//            return false;
//        }
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(10000));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(10000));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }

        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}