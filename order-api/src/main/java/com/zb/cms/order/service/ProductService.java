package com.zb.cms.order.service;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.product.AddProductForm;
import com.zb.cms.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //상품추가
    @Transactional
    public Product addproduct(Long sellerId, AddProductForm form){
        return productRepository.save(Product.of(sellerId,form));
    }
}
