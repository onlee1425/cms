package com.zb.cms.user.service;

import com.zb.cms.user.domain.SignUpForm;
import com.zb.cms.user.domain.model.Customer;
import com.zb.cms.user.service.customer.SignUpCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService service;

    @Test
    void signUp(){
        SignUpForm form = SignUpForm.builder()
                .name("name")
                .birth(LocalDate.now())
                .email("abc@gmail.com")
                .password("1")
                .phone("010-0000-0000")
                .build();
        Customer c = service.signUp(form);
       assertNotNull(c.getId());
       assertNotNull(c.getCreateAt());
    }
}