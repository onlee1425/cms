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

    //회원가입
    @PostMapping
    public ResponseEntity<String> customerSignUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }

    //verify
    @PutMapping("/verify/customer")
    public ResponseEntity<String> verifyCustomer(String email,String code){
        signUpApplication.customerVerify(email,code);
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }

}
