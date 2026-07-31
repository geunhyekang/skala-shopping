package com.skala.skala_shopping.dto.order;

import java.util.List;

public record CustomerOrderResponse(
    String customerId,
    long point,
    List<OrderItemResponse> orders
) {
}