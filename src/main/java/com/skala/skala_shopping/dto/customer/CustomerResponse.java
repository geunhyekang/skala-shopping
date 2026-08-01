package com.skala.skala_shopping.dto.customer;

import com.skala.skala_shopping.domain.customer.Customer;

public record CustomerResponse(Long id, String customerId, long point, String role) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getCustomerId(),
                customer.getPoint(),
                customer.getRole().name()
        );
    }
}
