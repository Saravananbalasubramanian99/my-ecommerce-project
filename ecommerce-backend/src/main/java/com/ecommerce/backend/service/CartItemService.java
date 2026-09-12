package com.ecommerce.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.exception.ResourceNotFoundException;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Get user's cart
    public List<CartItem> getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        return cartItemRepository.findByUser(user);
    }

    // Add product to cart
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId));

        // Check if product is already in cart
        CartItem existingItem =
                cartItemRepository.findByUserAndProduct(user, product)
                        .orElse(null);

        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity() + quantity
            );

            return cartItemRepository.save(existingItem);
        }

        CartItem newItem = new CartItem(user, product, quantity);

        return cartItemRepository.save(newItem);
    }

    // Update quantity
    public CartItem updateQuantity(
            Long userId,
            Long cartItemId,
            Integer quantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: " + cartItemId));

        // Make sure the cart item belongs to this user
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("You cannot modify this cart item");
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // Remove item
    public void removeFromCart(Long userId, Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: " + cartItemId));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("You cannot remove this cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    // Clear cart
    public void clearCart(Long userId) {

        List<CartItem> cartItems = getCart(userId);

        cartItemRepository.deleteAll(cartItems);
    }
}