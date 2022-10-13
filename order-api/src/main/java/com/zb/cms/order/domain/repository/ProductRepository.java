package com.zb.cms.order.domain.repository;

import com.zb.cms.order.domain.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, ProductRepositoryCustom{

    //productItems가 Load 되도록 설정
    @EntityGraph(attributePaths = {"productItems"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findWithProductItemsById(Long id);

    @EntityGraph(attributePaths = {"productItems"},type = EntityGraph.EntityGraphType.LOAD)
    Optional<Product> findBySellerIdAndId(Long sellerId, Long id);



}
