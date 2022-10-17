package com.zb.cms.order.application;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.product.AddProductCartForm;
import com.zb.cms.order.domain.product.AddProductForm;
import com.zb.cms.order.domain.product.AddProductItemForm;
import com.zb.cms.order.domain.redis.Cart;
import com.zb.cms.order.domain.repository.ProductRepository;
import com.zb.cms.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CartApplicationTest {
    @Autowired
    private CartApplication cartApplication;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void ADD_TEST() {
        Long customerId = 100L;

        cartApplication.clearCart(customerId);

        Product p = add_product();
        Product result = productRepository.findWithProductItemsById(p.getId()).get();

        assertNotNull(result);

        assertEquals(result.getName(), ("나이키 에어포스"));
        assertEquals(result.getDescription(), ("신발"));

        //추가검증
        assertEquals(result.getProductItems().size(), (3));
        assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
        assertEquals(result.getProductItems().get(0).getPrice(), 10000);
//        assertEquals(result.getProductItems().get(0).getCount(), 1);

        makeAddForm(result);
        Cart cart = cartApplication.addCart(customerId,makeAddForm(result));

        //데이터 검증
        assertEquals(cart.getMessages().size(),0);

        cart = cartApplication.getCart(customerId);
        assertEquals(cart.getMessages().size(),1);
    }

    //카트에 추가
    AddProductCartForm makeAddForm(Product p) {
        AddProductCartForm.ProductItem productItem =
                AddProductCartForm.ProductItem.builder()
                        .id(p.getProductItems().get(0).getId())
                        .name(p.getProductItems().get(0).getName())
                        .count(5)
                        .price(2000)
                        .build();
        return AddProductCartForm.builder()
                .id(p.getId())
                .sellerId(p.getSellerId())
                .name(p.getName())
                .description(p.getDescription())
                .items(List.of(productItem))
                .build();
    }

    Product add_product() {
        Long sellerid = 1L;

        AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);

        return productService.addProduct(sellerid, form);


    }

    private static AddProductForm makeProductForm(String name, String description, int itemCount) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(null, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();
    }

    private static AddProductItemForm makeProductItemForm(Long productId, String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000)
                .count(10)
                .build();
    }
}