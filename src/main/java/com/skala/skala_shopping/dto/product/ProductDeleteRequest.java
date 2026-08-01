package com.skala.skala_shopping.dto.product;

import jakarta.validation.constraints.NotNull;

public record ProductDeleteRequest(
    @NotNull(message = "상품 ID 는 필수입니다.")
    Long id
) {
}
