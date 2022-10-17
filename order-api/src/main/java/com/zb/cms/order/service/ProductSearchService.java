package com.zb.cms.order.service;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.repository.ProductRepository;
import com.zb.cms.order.exception.CustomException;
import com.zb.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductRepository productRepository;

    //아이템 페이지 확인
    public Product getByProductId(Long productId){
        return productRepository.findWithProductItemsById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    //아이템 목록
    public List<Product> getListByProductIds(List<Long> productIds){
        return productRepository.findAllByIdIn(productIds);
    }

    //검색을 통한 조회
    public List<Product> searchByName(String name){
        return productRepository.searchByName(name);
    }
}
