package com.zb.cms.order.controller;

import com.zb.cms.order.application.CartApplication;
import com.zb.cms.order.domain.product.AddProductCartForm;
import com.zb.cms.order.domain.redis.Cart;
import com.zb.cms.order.service.CartService;
import com.zb.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(
            @RequestHeader(name="X-AUTH-TOKEN") String token,
            @RequestBody AddProductCartForm form) {
        return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(),form));
    }

    @GetMapping
    public ResponseEntity<Cart> showCart(
            @RequestHeader(name="X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(cartApplication.getCart(provider.getUserVo(token).getId()));
    }

    @PutMapping
    public ResponseEntity<Cart> updateCart(
            @RequestHeader(name = "X-AUTH-TOKEN") String token,
            @RequestBody Cart cart){
        return ResponseEntity.ok(cartApplication.updateCart(provider.getUserVo(token).getId(),cart));
    }
}
