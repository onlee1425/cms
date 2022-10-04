package com.zb.cms.user.application;

import com.zb.cms.user.client.MailgunClient;
import com.zb.cms.user.client.mailgun.SendMailForm;
import com.zb.cms.user.domain.SignUpForm;
import com.zb.cms.user.domain.model.Customer;
import com.zb.cms.user.domain.model.Seller;
import com.zb.cms.user.exception.CustomException;
import com.zb.cms.user.exception.ErrorCode;
import com.zb.cms.user.service.customer.SignUpCustomerService;
import com.zb.cms.user.service.seller.SignUpSellerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SignUpSellerService signUpSellerService;

    //고객
    public void customerVerify(String email,String code){
        signUpCustomerService.verifyEmail(email,code);
    }

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
            // exception
        } else {
            Customer c = signUpCustomerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();

            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("onlee1425@naver.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(form.getEmail(), c.getName(),"customer", code))
                    .build();

            mailgunClient.sendEmail(sendMailForm);
            signUpCustomerService.ChangeCustomerValidationEmail(c.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    //셀러
    public void sellerVerify(String email,String code){
        signUpSellerService.verifyEmail(email,code);
    }

    public String sellerSignUp(SignUpForm form){
        if (signUpSellerService.isEmailExist(form.getEmail())){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }
        //예외처리
        else {
            Seller s = signUpSellerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();

            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("onlee1425@naver.com")
                    .to(form.getEmail())
                    .subject("~Verification Email~")
                    .text(getVerificationEmailBody(form.getEmail(),s.getName(),"seller",code))
                    .build();

            mailgunClient.sendEmail(sendMailForm);
            signUpSellerService.ChangeSellerValidationEmail(s.getId(),code);
            return "회원 가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name,String type, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello").append(name).append("! Please Click Link for verification.\n\n")
                .append("http://localhost:8080/signup/"+type+"/verify?email=")
                .append(email)
                .append("&code=")

                .append(code).toString();

    }

}
