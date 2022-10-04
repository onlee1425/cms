package com.zb.cms.user.domain.seller;

import com.zb.cms.user.domain.model.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SellerDto {

    private String email;
    private Long id;

    public static SellerDto from(Seller seller){
        return new SellerDto(seller.getEmail(),seller.getId());
    }
}
