package com.zb.cms.order.domain.repository;

import com.zb.cms.order.domain.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem,Long> {
}
