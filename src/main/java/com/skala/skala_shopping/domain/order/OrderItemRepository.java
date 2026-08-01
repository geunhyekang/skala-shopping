package com.skala.skala_shopping.domain.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skala.skala_shopping.domain.customer.Customer;
import com.skala.skala_shopping.domain.product.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByCustomerAndProduct(Customer customer, Product product);

    List<OrderItem> findAllByCustomer(Customer customer);

    void deleteAllByCustomer(Customer customer);
}