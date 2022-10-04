package com.zb.cms.user.controller;

import com.zb.cms.user.application.SignUpApplication;
import com.zb.cms.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpApplication signUpApplication;

    //고객-회원가입
    @PostMapping("/customer")
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    //고객-verify
    @GetMapping("/customer/verify")
    public ResponseEntity<String> verifyCustomer(String email,String code){
        signUpApplication.customerVerify(email,code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

    //셀러-회원가입
    @PostMapping("/seller")
    public ResponseEntity<String> sellerSignUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
    }

    //셀러-verify
    @GetMapping("/seller/verify")
    public ResponseEntity<String> verifySeller(String email,String code){
        signUpApplication.sellerVerify(email,code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

}
