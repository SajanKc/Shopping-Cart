package com.iamsajan.shoppingcart.controller;

import com.iamsajan.shoppingcart.dto.ApiResponse;
import com.iamsajan.shoppingcart.dto.order.OrderDto;
import com.iamsajan.shoppingcart.exceptions.ResourceNotFoundException;
import com.iamsajan.shoppingcart.model.Order;
import com.iamsajan.shoppingcart.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/add/{userId}")
    private ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Item order success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occurred!", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    private ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Get order success!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> userOrders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Get user order success", userOrders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Oops!", e.getMessage()));
        }
    }
}
