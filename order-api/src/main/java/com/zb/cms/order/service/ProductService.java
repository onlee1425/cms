package com.zb.cms.order.service;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.model.ProductItem;
import com.zb.cms.order.domain.product.AddProductForm;
import com.zb.cms.order.domain.product.UpdateProductForm;
import com.zb.cms.order.domain.product.UpdateProductItemForm;
import com.zb.cms.order.domain.repository.ProductRepository;
import com.zb.cms.order.exception.CustomException;
import com.zb.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //상품추가
    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form){
        return productRepository.save(Product.of(sellerId,form));
    }

    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form){
        Product product = productRepository.findBySellerIdAndId(sellerId,form.getId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        product.setDescription(form.getDescription());
        product.setName(form.getName());

        for (UpdateProductItemForm itemForm: form.getItems()){
            ProductItem item = product.getProductItems().stream()
                    .filter(pi->pi.getId().equals(itemForm.getId()))
                    .findFirst().orElseThrow(()->new CustomException(ErrorCode.NOT_FOUNT_ITEM));
            item.setName(itemForm.getName());
            item.setPrice(itemForm.getPrice());
            item.setCount(itemForm.getCount());
        }
        return product;
    }
}
