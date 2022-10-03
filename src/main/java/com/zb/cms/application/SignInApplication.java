package com.zb.cms.application;

import com.zb.cms.domain.model.Customer;
import com.zb.cms.domain.repository.SignInForm;
import com.zb.cms.exception.CustomException;
import com.zb.cms.service.CustomerService;
import com.zb.domain.config.JwtAuthenticationProvider;
import com.zb.domain.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zb.cms.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;

    public String customerLoginToken(SignInForm form){
        // 1. 로그인 가능 여부 확인
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

        // 2. 토큰 발행
        

        // 3. 토큰 response
        return provider.createToken(c.getEmail(),c.getId(), UserType.CUSTOMER);

    }
}
