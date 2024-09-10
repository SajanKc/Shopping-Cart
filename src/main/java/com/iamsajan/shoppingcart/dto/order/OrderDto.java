package com.iamsajan.shoppingcart.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    private Set<OrderItemDto> items;
}
