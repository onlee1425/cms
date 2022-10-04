package com.zb.cms.user.controller;

import com.zb.cms.user.application.SignInApplication;
import com.zb.cms.user.domain.repository.SignInForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/signin")
@RequiredArgsConstructor
public class SignInController {

    private final SignInApplication signInApplication;

    // 고객
    @PostMapping("/customer")
    public ResponseEntity<String> signInCustomer(@RequestBody SignInForm form){
        return ResponseEntity.ok(signInApplication.customerLoginToken(form));
    }

    // 셀러
    @PostMapping("/seller")
    public ResponseEntity<String> signInSeller(@RequestBody SignInForm form){
        return ResponseEntity.ok(signInApplication.sellerLoginToken(form));
    }
}
