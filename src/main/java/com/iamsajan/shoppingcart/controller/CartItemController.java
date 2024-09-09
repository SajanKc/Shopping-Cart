package com.iamsajan.shoppingcart.controller;

import com.iamsajan.shoppingcart.dto.ApiResponse;
import com.iamsajan.shoppingcart.exceptions.ResourceNotFoundException;
import com.iamsajan.shoppingcart.service.cart.ICartItemService;
import com.iamsajan.shoppingcart.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/cartitem")
@RequiredArgsConstructor
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping("/add")
    private ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                      @RequestParam Long productId,
                                                      @RequestParam Integer quantity) {
        try {
            if (cartId == null)
                cartId = cartService.initializeNewCart();

            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item add success", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove/cart/{cartId}/product/{productId}")
    private ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                           @PathVariable Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Remove item success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/product/{productId}/quantity/{quantity}")
    private ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                           @PathVariable Long productId,
                                                           @PathVariable Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update item success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
