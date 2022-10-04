package com.zb.cms.user.service.customer;

import com.zb.cms.user.domain.SignUpForm;
import com.zb.cms.user.domain.model.Customer;
import com.zb.cms.user.domain.repository.CustomerRepository;
import com.zb.cms.user.exception.CustomException;
import com.zb.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    //email verification
    public boolean isEmailExist(String email) {
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();
    }

    //회원 인증메일
    @Transactional
    public LocalDateTime ChangeCustomerValidationEmail(Long customerId, String verificationCode) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setVerificationCode(verificationCode);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1)); //하루 뒤에 만료
            return customer.getVerifyExpiredAt();
        }
        //예외처리
        throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }

    @Transactional
    //이메일에 대한 verify
    public void verifyEmail(String email,String code){
        Customer customer = customerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (customer.isVerify()){
            throw new CustomException(ErrorCode.ALREADY_VERIFY); //이미 인증완료
        }
        else if (!customer.getVerificationCode().equals(code)){
            throw new CustomException(ErrorCode.WRONG_VERIFICATION); //회원 - 인증코드 불일치
        }
        else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRE_CODE); //인증코드 만료
        }
        customer.setVerify(true);
    }
}
