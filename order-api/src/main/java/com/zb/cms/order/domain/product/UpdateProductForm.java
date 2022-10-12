package com.zb.cms.order.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductForm {
    private String name;
    private Long id;
    private String description;
    private List<UpdateProductItemForm> items;
}
