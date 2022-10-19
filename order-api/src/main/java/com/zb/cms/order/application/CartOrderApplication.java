package com.zb.cms.order.application;

import com.zb.cms.order.client.UserClient;
import com.zb.cms.order.client.user.ChangeBalanceForm;
import com.zb.cms.order.client.user.CustomerDto;
import com.zb.cms.order.domain.model.ProductItem;
import com.zb.cms.order.domain.redis.Cart;
import com.zb.cms.order.exception.CustomException;
import com.zb.cms.order.exception.ErrorCode;
import com.zb.cms.order.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CartOrderApplication {

    private final CartApplication cartApplication;
    private final UserClient userClient;

    private final ProductItemService productItemService;

    @Transactional
    public void order(String token, Cart cart) {

        Cart orderCart = cartApplication.refreshCart(cart);
        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ErrorCode.ORDER_FAIL_CHECK_CART);
        }
        //고객 잔액 확인
        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        int totalPrice = getTotalPrice(cart);
        if (customerDto.getBalance()<totalPrice){
            throw new CustomException(ErrorCode.ORDER_FAIL_NO_MONEY);
        }
        // 결제 (롤백 계획에 대해 생각해야함.)
        userClient.changeBalance(token,
                ChangeBalanceForm.builder()
                        .from("USER")
                        .message("Order")
                        .money(-totalPrice)
                        .build());
        // 결제상품 재고 관리
        for (Cart.Product product : orderCart.getProducts()){
            for (Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount()-cartItem.getCount());
            }
        }
    }


    private Integer getTotalPrice(Cart cart) {

        return cart.getProducts().stream().flatMapToInt(
                product -> product.getItems().stream().flatMapToInt(
                        productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
                .sum();

    }
}
