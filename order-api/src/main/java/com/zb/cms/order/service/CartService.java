package com.zb.cms.order.service;

import com.zb.cms.order.client.RedisClient;
import com.zb.cms.order.domain.product.AddProductCartForm;
import com.zb.cms.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final RedisClient redisClient;

    //카트 조회
    public Cart getCart(Long customerId){
        Cart cart = redisClient.get(customerId,Cart.class);
        return cart!=null?cart:new Cart();
    }

    //카트 업데이트
    public Cart putCart(Long customerId,Cart cart){
         redisClient.put(customerId,cart);
         return cart;
    }


    public Cart addCart(Long customerId, AddProductCartForm form) {
        Cart cart = redisClient.get(customerId, Cart.class); //customerId와 Cart 테이블을 가져옴
        if (cart == null) { //카트가 null 일경우 새 카트를 만들고, customerId도 넣어줌
            cart = new Cart();
            cart.setCustomerId(customerId);
        }

        // 상품추가
        // 이전에 같은 상품 있는지 확인
        Optional<Cart.Product> productOptional = cart.getProducts().stream()
                .filter(product1 -> product1.getId().equals(form.getId()))
                .findFirst();
        //있을경우
        if (productOptional.isPresent()){
            Cart.Product redisProduct = productOptional.get();
            // requested
            List<Cart.ProductItem> items = form.getItems().stream().map(Cart.ProductItem::from).collect(Collectors.toList());
            Map<Long,Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, it->it));


            //상품정보 변동 - redisProduct의 이름 form에서 가져온 이름과 다를경우
            if (!redisProduct.getName().equals(form.getName())){
                cart.addMessage(redisProduct.getName()+"의 정보가 변경되었습니다. 확인 부탁드립니다.");
            }
            for (Cart.ProductItem item : items){
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());

                if (redisItem == null){
                    // happy case
                    redisProduct.getItems().add(item);
                }else {
                    if (!redisItem.getPrice().equals(item.getPrice())){
                        cart.addMessage(redisProduct.getName()+item.getName()+"의 가격이 변경되었습니다. 확인 부탁드립니다.");
                    }
                    redisItem.setCount(redisItem.getCount()+item.getCount());
                }
            }


        }else {
            //없을경우
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
            }
        redisClient.put(customerId,cart); //해당 고객의 cart에 추가
        return cart;

    }


}
