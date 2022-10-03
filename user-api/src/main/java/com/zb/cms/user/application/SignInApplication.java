package com.zb.cms.user.application;

import com.zb.cms.user.domain.model.Customer;
import com.zb.cms.user.domain.repository.SignInForm;
import com.zb.cms.user.exception.CustomException;
import com.zb.cms.user.service.CustomerService;
import com.zb.cms.user.exception.ErrorCode;
import com.zb.domain.config.JwtAuthenticationProvider;
import com.zb.domain.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;

    public String customerLoginToken(SignInForm form){
        // 1. 로그인 가능 여부 확인
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        // 2. 토큰 발행
        

        // 3. 토큰 response
        return provider.createToken(c.getEmail(),c.getId(), UserType.CUSTOMER);

    }
}
