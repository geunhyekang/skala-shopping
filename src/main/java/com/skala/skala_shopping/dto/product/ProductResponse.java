package com.skala.skala_shopping.dto.product;

import com.skala.skala_shopping.domain.product.Product;

public record ProductResponse(Long id, String name, long price) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}