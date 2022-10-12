package com.zb.cms.order;

import com.zb.cms.order.domain.product.AddProductForm;
import com.zb.cms.order.domain.product.AddProductItemForm;
import com.zb.cms.order.domain.product.ProductDto;
import com.zb.cms.order.domain.product.ProductItemDto;
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

        return ResponseEntity.ok(ProductDto.from(productService.addproduct(provider.getUserVo(token).getId(),form)));

    }

    //상품 내 item 추가
    @PostMapping("/item")
    public ResponseEntity<ProductDto> addProductItem(@RequestHeader(name = "X-AUTH-TOKEN") String token,
                                                             @RequestBody AddProductItemForm form) {

        return ResponseEntity.ok(ProductDto.from(productItemService.addProductItem(provider.getUserVo(token).getId(),form)));

    }
}
