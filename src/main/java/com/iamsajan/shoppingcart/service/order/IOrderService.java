package com.iamsajan.shoppingcart.service.order;

import com.iamsajan.shoppingcart.dto.order.OrderDto;
import com.iamsajan.shoppingcart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
