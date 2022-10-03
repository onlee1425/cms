package com.zb.cms.domain.customer;

import com.zb.cms.domain.model.Customer;
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
