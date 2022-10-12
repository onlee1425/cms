package com.zb.cms.order.service;

import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.product.AddProductForm;
import com.zb.cms.order.domain.product.AddProductItemForm;
import com.zb.cms.order.domain.repository.ProductItemRepository;
import com.zb.cms.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Test
    void ADD_PRODUCT_TEST() {
        Long sellerid = 1L;

        AddProductForm form = makeProductForm("나이키 에어포스", "신발", 3);

        Product p = productService.addproduct(sellerid, form);

        Product result = productRepository.findWithProductItemsById(p.getId()).get();

        assertNotNull(result);

        assertEquals(result.getName(),("나이키 에어포스"));
        assertEquals(result.getDescription(),("신발"));

        //추가검증
        assertEquals(result.getProductItems().size(),(3));
        assertEquals(result.getProductItems().get(0).getName(),"나이키 에어포스0");
        assertEquals(result.getProductItems().get(0).getPrice(),10000);
        assertEquals(result.getProductItems().get(0).getCount(),1);

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
                .count(1)
                .build();
    }

}