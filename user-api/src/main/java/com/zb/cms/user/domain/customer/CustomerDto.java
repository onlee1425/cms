package com.zb.cms.user.domain.customer;

import com.zb.cms.user.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto {

    private String email;
    private Long id;

    public static CustomerDto from(Customer customer){
       return new CustomerDto(customer.getEmail(),customer.getId());
    }
}
