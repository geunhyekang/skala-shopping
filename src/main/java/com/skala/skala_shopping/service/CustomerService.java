package com.skala.skala_shopping.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skala.skala_shopping.domain.customer.Customer;
import com.skala.skala_shopping.domain.customer.CustomerRepository;
import com.skala.skala_shopping.domain.order.OrderItemRepository;
import com.skala.skala_shopping.dto.customer.CustomerResponse;
import com.skala.skala_shopping.dto.customer.CustomerUpdateRequest;
import com.skala.skala_shopping.dto.order.CustomerOrderResponse;
import com.skala.skala_shopping.dto.order.OrderItemResponse;
import com.skala.skala_shopping.exception.BusinessException;
import com.skala.skala_shopping.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(
            CustomerRepository customerRepository,
            OrderItemRepository orderItemRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerResponse::from);
    }

    public CustomerResponse findByKey(String key) {
        return CustomerResponse.from(getCustomerByKey(key));
    }

    @Transactional
    public CustomerResponse update(String requesterId, boolean requesterIsAdmin, CustomerUpdateRequest request) {
        // 일반 고객은 본인 비밀번호만, 관리자는 누구의 비밀번호든 변경할 수 있습니다.
        if (!requesterIsAdmin && !request.customerId().equals(requesterId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        log.info("고객 정보 변경: customerId={}, requester={}", request.customerId(), requesterId);
        Customer customer = customerRepository.findByCustomerId(request.customerId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
        customer.changePassword(passwordEncoder.encode(request.password()));
        return CustomerResponse.from(customer);
    }

    @Transactional
    public void delete(String key) {
        Customer customer = getCustomerByKey(key);
        log.info("고객 삭제: customerId={}", customer.getCustomerId());
        // 고객이 보유한 주문 내역을 먼저 지워야 참조 무결성이 깨지지 않습니다.
        orderItemRepository.deleteAllByCustomer(customer);
        customerRepository.delete(customer);
    }

    public CustomerOrderResponse findCustomerProducts(String key) {
        Customer customer = getCustomerByKey(key);
        List<OrderItemResponse> orders = orderItemRepository.findAllByCustomer(customer).stream()
                .map(OrderItemResponse::from)
                .toList();
        return new CustomerOrderResponse(customer.getCustomerId(), customer.getPoint(), orders);
    }

    // 명세의 {customerId} 와 {customerName} 은 URI 가 같아 하나의 경로 변수로 받습니다.
    // 문자열 고객 ID(이름) 우선 조회, 실패 시 숫자면 PK 로 조회합니다.
    private Customer getCustomerByKey(String key) {
        return customerRepository.findByCustomerId(key)
                .or(() -> findByNumericId(key))
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    private Optional<Customer> findByNumericId(String key) {
        try {
            return customerRepository.findById(Long.parseLong(key));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
