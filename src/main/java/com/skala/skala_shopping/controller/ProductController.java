package com.skala.skala_shopping.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skala.skala_shopping.dto.product.ProductDeleteRequest;
import com.skala.skala_shopping.dto.product.ProductRequest;
import com.skala.skala_shopping.dto.product.ProductResponse;
import com.skala.skala_shopping.dto.product.ProductUpdateRequest;
import com.skala.skala_shopping.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 전체 목록 조회 (페이지 단위 처리: ?page=0&size=10&sort=id,asc)
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(request));
    }

    // 명세에 따라 수정 대상 ID 는 경로가 아닌 요청 본문으로 받습니다.
    @PutMapping
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.update(request));
    }

    // 명세에 따라 삭제 대상 ID 는 경로가 아닌 요청 본문으로 받습니다.
    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody ProductDeleteRequest request) {
        productService.delete(request.id());
        return ResponseEntity.noContent().build();
    }
}
