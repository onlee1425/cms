package com.zb.cms.user.application;

import com.zb.cms.user.domain.model.Customer;
import com.zb.cms.user.domain.model.Seller;
import com.zb.cms.user.domain.repository.SignInForm;
import com.zb.cms.user.exception.CustomException;
import com.zb.cms.user.service.customer.CustomerService;
import com.zb.cms.user.exception.ErrorCode;
import com.zb.cms.user.service.seller.SellerService;
import com.zb.domain.config.JwtAuthenticationProvider;
import com.zb.domain.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final JwtAuthenticationProvider provider;

    //고객
    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부 확인
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        // 2. 토큰 발행


        // 3. 토큰 response
        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);

    }

    //셀러
    public String sellerLoginToken(SignInForm form) {
        Seller s = sellerService.findValidSeller(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        return provider.createToken(s.getEmail(),s.getId(),UserType.SELLER);
    }

}
