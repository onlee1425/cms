package com.zb.cms.order.domain.product;

import com.zb.cms.order.domain.model.Product;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductItemDto> items;

    public static ProductDto from(Product product){
        List<ProductItemDto> items = product.getProductItems()
                .stream().map(ProductItemDto::from).collect(Collectors.toList());

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .items(items)
                .build();
    }

    //searchByName 용(검색)
    public static ProductDto withoutItemsfrom(Product product){

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }
}
