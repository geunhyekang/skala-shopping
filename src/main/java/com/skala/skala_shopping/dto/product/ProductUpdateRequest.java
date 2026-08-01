package com.skala.skala_shopping.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
    @NotNull(message = "상품 ID 는 필수입니다.")
    Long id,

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 100, message = "상품명은 100자 이하여야 합니다.")
    String name,

    @Positive(message = "상품 가격은 1원 이상이어야 합니다.")
    long price
) {
}
