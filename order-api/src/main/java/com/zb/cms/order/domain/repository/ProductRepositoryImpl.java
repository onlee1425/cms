package com.zb.cms.order.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.cms.order.domain.model.Product;
import com.zb.cms.order.domain.model.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchByName(String name) {
        String search = "%"+name+"%";

        //search의 이름과 같은 product의 name을 조회
        QProduct product = QProduct.product;
        return queryFactory.selectFrom(product)
                .where(product.name.like(search))
                .fetch();
    }
}
