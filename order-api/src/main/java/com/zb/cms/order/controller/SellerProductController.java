package com.zb.cms.order.controller;

import com.zb.cms.order.domain.product.*;
import com.zb.cms.order.service.ProductItemService;
import com.zb.cms.order.service.ProductService;
import com.zb.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final JwtAuthenticationProvider provider;
    private final ProductItemService productItemService;

    // 상품 추가
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                           @RequestBody AddProductForm form) {

        return ResponseEntity.ok(ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(),form)));

    }

    //상품 내 item 추가
    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                             @RequestBody AddProductItemForm form) {

        return ResponseEntity.ok(ProductDto.from(productItemService.addProductItem(provider.getUserVo(token).getId(),form)));

    }

    // 상품 업데이트
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                    @RequestBody UpdateProductForm form) {

        return ResponseEntity.ok(ProductDto.from(productService.updateProduct(provider.getUserVo(token).getId(),form)));

    }

    //item 업데이트
    @PutMapping("/item")
    public ResponseEntity<ProductItemDto> updateProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                     @RequestBody UpdateProductItemForm form) {

        return ResponseEntity.ok(ProductItemDto.from(productItemService.updateProductItem(provider.getUserVo(token).getId(),form)));

    }

    // 상품 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                    @RequestParam Long id) {
        productService.deleteProduct(provider.getUserVo(token).getId(),id);
        return ResponseEntity.ok().build();

    }

    // item 삭제
    @DeleteMapping("/item")
    public ResponseEntity<Void> deleteProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                  @RequestParam Long id) {
        productItemService.deleteProductItem(provider.getUserVo(token).getId(),id);
        return ResponseEntity.ok().build();

    }
}
