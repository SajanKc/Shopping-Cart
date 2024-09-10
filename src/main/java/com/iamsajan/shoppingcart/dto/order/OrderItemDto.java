package com.iamsajan.shoppingcart.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
