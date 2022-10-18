package com.zb.cms.order.application;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.model.ProductItem;
import com.zb.cms.order.domain.product.AddProductCartForm;
import com.zb.cms.order.domain.redis.Cart;
import com.zb.cms.order.exception.CustomException;
import com.zb.cms.order.exception.ErrorCode;
import com.zb.cms.order.service.CartService;
import com.zb.cms.order.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartApplication {
    private final CartService cartService;
    private final ProductSearchService productSearchService;

    //cart 추가
    public Cart addCart(Long customerId, AddProductCartForm form) {
        Product product = productSearchService.getByProductId(form.getId());
        if (product == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_PRODUCT);
        }
        Cart cart = cartService.getCart(customerId);
        if (cart != null && !addAble(cart, product, form)) {
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    // 장바구니 변경
    public Cart updateCart(Long customerId,Cart cart){
       cartService.putCart(customerId,cart);
       return getCart(customerId);
    }




    //cart 조회
    /*
    장바구니 상품 조회
     */
    public Cart getCart(Long customerId) {
        Cart cart = refreshCart(cartService.getCart(customerId));
        Cart returnCart = new Cart();
        returnCart.setCustomerId(customerId);
        returnCart.setProducts(cart.getProducts());
        returnCart.setMessages(cart.getMessages());
        cart.setMessages(new ArrayList<>());
        //메세지가 없는 경우
        cartService.putCart(customerId,cart);
        return returnCart;

        // 2. 메세지를 보고 난 다음에는, 이미 본 스팸 메세지를 제거
    }

    public void clearCart(Long customerId){
        cartService.putCart(customerId,null);
    }

    private Cart refreshCart(Cart cart) {
        // 1. 상품 , 상품의 아이템 정보, 가격, 수량이 변경되었는지 체크 - 변동이 있을경우 알람
        // 2. 변동된 경우 상품의 구매가능한 수량, 변동된 가격으로 장바구니를 업데이트.
        Map<Long, Product> productMap = productSearchService.getListByProductIds(
                        cart.getProducts().stream().map(Cart.Product::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        //상품 가져오기
        for (int i = 0; i < cart.getProducts().size(); i++) {

            Cart.Product cartProduct = cart.getProducts().get(i);

            Product p = productMap.get(cartProduct.getId());

            if (p == null) {
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName() + "상품이 삭제되었습니다.");
                continue;
            }

            Map<Long, ProductItem> productItemMap = p.getProductItems().stream()
                    .collect(Collectors.toMap(ProductItem::getId, productItem -> productItem));

            // 케이스별 에러체크
            List<String> tmpMessages = new ArrayList<>();
            for (int j = 0; j < cartProduct.getItems().size(); j++) {
                Cart.ProductItem cartProductItem = cartProduct.getItems().get(j);
                ProductItem pi = productItemMap.get(cartProductItem.getId()); //동시에 에러케이스가 발생할경우를 위해 임시변수에 저장

                //아이템이 없을경우
                if (pi == null) {
                    cartProduct.getItems().remove(cartProductItem); //장바구니에서 삭제
                    j--;
                    tmpMessages.add(cartProductItem.getName() + "옵션이 삭제되었습니다.");
                    continue;
                }

                boolean isPriceChanged = false, isCountNotEnough = false;
                //가격이 변동된경우
                if (!cartProductItem.getPrice().equals(productItemMap.get(cartProductItem.getId()).getPrice())) {
                    isPriceChanged = true;
                    cartProductItem.setPrice(pi.getPrice());
                }
                //재고가 변경된경우
                if (cartProductItem.getCount() > pi.getCount()) {
                    isCountNotEnough = true;
                    cartProductItem.setCount(pi.getCount());
                }
                //가격과 재고가 부족한경우
                if (isPriceChanged && isCountNotEnough) {
                    // message 1
                    tmpMessages.add(cartProductItem.getName() + "가격 변동 및 재고가 부족하여 구매 가능한 최대 수량으로 변경되었습니다.");
                } else if (isPriceChanged) {
                    // message 2
                    tmpMessages.add(cartProductItem.getName() + "가격이 변동되었습니다.");
                } else if (isCountNotEnough) {
                    // message 3
                    tmpMessages.add(cartProductItem.getName() + "재고가 부족하여 구매 가능한 최대 수량으로 변경되었습니다.");
                }
            }
            //상품의 아이템이 모두 삭제된경우
            if (cartProduct.getItems().size()==0){
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMessage(cartProduct.getName()+"상품의 옵션이 모두 없어져 구매가 불가능합니다.");
            } else if (tmpMessages.size()>0) {
                StringBuilder builder = new StringBuilder();
                builder.append(cartProduct.getName()+"상품의 변동 사항 : ");
                for (String message: tmpMessages){
                    builder.append(message);
                    builder.append(",");
                }
                cart.addMessage(builder.toString());
            }
        }
        cartService.putCart(cart.getCustomerId(),cart);
        return cart;
    }


    //cart 추가가능 여부 확인
    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
        Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst().orElse(Cart.Product.builder().id(product.getId())
                        .items(Collections.emptyList()).build());

        Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
                .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

        Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
                .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

        return form.getItems().stream().noneMatch(
                formItem -> {
                    Integer cartCount = cartItemCountMap.get(formItem.getId());
                    if (cartCount == null){
                        cartCount = 0;
                    }
                    Integer currentCount = currentItemCountMap.get(formItem.getId());
                    return formItem.getCount() + cartCount > currentCount;
                });
    }
}
