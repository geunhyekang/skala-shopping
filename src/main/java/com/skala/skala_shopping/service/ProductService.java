package com.skala.skala_shopping.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skala.skala_shopping.domain.product.Product;
import com.skala.skala_shopping.domain.product.ProductRepository;
import com.skala.skala_shopping.dto.product.ProductRequest;
import com.skala.skala_shopping.dto.product.ProductResponse;
import com.skala.skala_shopping.exception.BusinessException;
import com.skala.skala_shopping.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    //이 클래스 전용 logger -> slf4j 인터페이스로 실제 기록은 logback이 수행
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository) {
        // 생성자 주입을 사용하면 Service 가 저장소 구현을 직접 만들지 않습니다.
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse findById(Long id) {
        log.debug("상품 단건 조회 요청: id={}", id);
        return ProductResponse.from(getProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.info("상품 등록: name={}, price={}", request.name(), request.price());
        Product product = new Product(request.name(), request.price());
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        log.info("상품 수정: id={}, name={}, price={}", id, request.name(), request.price());
        Product product = getProduct(id);
        product.update(request.name(), request.price());
        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        log.info("상품 삭제: id={}", id);
        //Product product = getProduct(id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}