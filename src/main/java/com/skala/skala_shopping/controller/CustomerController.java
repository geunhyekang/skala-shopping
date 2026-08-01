package com.skala.skala_shopping.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skala.skala_shopping.dto.customer.CustomerResponse;
import com.skala.skala_shopping.dto.customer.CustomerUpdateRequest;
import com.skala.skala_shopping.dto.order.CustomerOrderResponse;
import com.skala.skala_shopping.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 고객 전체 목록 조회 (페이지 단위 처리: ?page=0&size=10&sort=id,asc)
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> findAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    // 명세의 {customerId} / {customerName} 조회를 하나의 경로 변수로 처리합니다.
    // 문자열 고객 ID(이름) 우선, 실패 시 숫자 PK 로 조회합니다.
    @GetMapping("/{key}")
    public ResponseEntity<CustomerResponse> findByKey(@PathVariable String key) {
        return ResponseEntity.ok(customerService.findByKey(key));
    }

    // 고객 정보 변경 (변경 대상은 요청 본문의 customerId 로 지정)
    // 일반 고객은 본인만, 관리자는 누구든 변경할 수 있습니다.
    @PutMapping
    public ResponseEntity<CustomerResponse> update(
            Authentication authentication,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        return ResponseEntity.ok(
                customerService.update(authentication.getName(), isAdmin, request));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable String customerId) {
        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }

    // 고객의 상품(주문) 정보 조회
    @GetMapping("/{customerId}/products")
    public ResponseEntity<CustomerOrderResponse> findCustomerProducts(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.findCustomerProducts(customerId));
    }
}
