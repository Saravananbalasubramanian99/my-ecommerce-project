package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.entity.CartItem;
import com.ecommerce.backend.service.CartItemService;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    // Get cart
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartItemService.getCart(userId)
        );
    }

    // Add product
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartItem> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartItemService.addToCart(
                        userId,
                        productId,
                        quantity
                )
        );
    }

    // Update quantity
    @PutMapping("/{userId}/{cartItemId}")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartItemService.updateQuantity(
                        userId,
                        cartItemId,
                        quantity
                )
        );
    }

    // Remove item
    @DeleteMapping("/{userId}/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {

        cartItemService.removeFromCart(userId, cartItemId);

        return ResponseEntity.noContent().build();
    }

    // Clear cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(
            @PathVariable Long userId) {

        cartItemService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }
}