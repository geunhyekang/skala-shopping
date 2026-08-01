package com.skala.skala_shopping.dto.customer;

public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresInMinutes,
    String role
) {
}