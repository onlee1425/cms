package com.zb.cms.controller;

import com.zb.cms.domain.customer.CustomerDto;
import com.zb.cms.domain.model.Customer;
import com.zb.cms.exception.CustomException;
import com.zb.cms.exception.ErrorCode;
import com.zb.cms.service.CustomerService;
import com.zb.domain.config.JwtAuthenticationProvider;
import com.zb.domain.domain.common.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name="X-AUTH-TOKEN")String token){
        UserVo vo =  provider.getUserVo(token);
        Customer c = customerService.findByIdAndEmail(vo.getId(),vo.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return ResponseEntity.ok(CustomerDto.from(c));
    }

}
