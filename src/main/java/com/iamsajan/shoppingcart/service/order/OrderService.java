package com.iamsajan.shoppingcart.service.order;

import com.iamsajan.shoppingcart.dto.order.OrderDto;
import com.iamsajan.shoppingcart.dto.order.OrderItemDto;
import com.iamsajan.shoppingcart.enums.OrderStatus;
import com.iamsajan.shoppingcart.exceptions.ResourceNotFoundException;
import com.iamsajan.shoppingcart.model.Cart;
import com.iamsajan.shoppingcart.model.Order;
import com.iamsajan.shoppingcart.model.OrderItem;
import com.iamsajan.shoppingcart.model.Product;
import com.iamsajan.shoppingcart.repository.OrderRepository;
import com.iamsajan.shoppingcart.repository.ProductRepository;
import com.iamsajan.shoppingcart.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        // get cart by userId
        Cart cart = cartService.getCartByUserId(userId);
        // create order with cart
        Order order = createOrder(cart);
        // create orderItem using cart and order
        List<OrderItem> orderItemList = createOrderItems(cart, order);
        // set other value of order
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        // clear cart
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                }).toList();
    }

    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::orderDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        return orderList.stream().map(this::orderDtoMapper).toList();
    }

    public OrderDto orderDtoMapper(Order order) {
        return OrderDto.builder()
                .id(order.getOrderId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(String.valueOf(order.getOrderStatus()))
                .items(order.getOrderItems()
                        .stream()
                        .map(this::orderItemDtoMapper)
                        .collect(Collectors.toSet()))
                .build();
    }

    public OrderItemDto orderItemDtoMapper(OrderItem orderItem) {
        return OrderItemDto.builder()
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
