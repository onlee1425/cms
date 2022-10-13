package com.zb.cms.order.domain.repository;

import com.zb.cms.order.domain.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchByName(String name);
}
