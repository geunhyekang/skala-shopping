package com.skala.skala_shopping.dto.order;

public record OrderResultResponse(
    String message,
    long remainingPoint,
    Long productId,
    String productName,
    int quantity
) {
}