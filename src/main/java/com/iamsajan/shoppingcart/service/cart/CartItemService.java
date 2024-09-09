package com.iamsajan.shoppingcart.service.cart;

import com.iamsajan.shoppingcart.exceptions.ResourceNotFoundException;
import com.iamsajan.shoppingcart.model.Cart;
import com.iamsajan.shoppingcart.model.CartItem;
import com.iamsajan.shoppingcart.model.Product;
import com.iamsajan.shoppingcart.repository.CartItemRepository;
import com.iamsajan.shoppingcart.repository.CartRepository;
import com.iamsajan.shoppingcart.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. Get the product
        //3. Check if the product already in the cart
        //4. If Yes, then increase the quantity with the requested quantity
        //5. If No, then initiate a new CartItem entry.
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());

        if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemRemove = getCartItem(cartId, productId);
        cart.removeItem(itemRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(
                        item -> {
                            item.setQuantity(quantity);
                            item.setUnitPrice(item.getProduct().getPrice());
                            item.setTotalPrice(); // calculate in model class
                        }
                );

        BigDecimal totalAmount = cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found"));
    }
}
