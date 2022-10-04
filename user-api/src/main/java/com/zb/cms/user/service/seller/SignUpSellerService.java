package com.zb.cms.user.service.seller;

import com.zb.cms.user.domain.SignUpForm;
import com.zb.cms.user.domain.model.Seller;
import com.zb.cms.user.domain.repository.SellerRepository;
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
public class SignUpSellerService {
    private final SellerRepository sellerRepository;

    //가입
    public Seller signUp(SignUpForm form){
        return sellerRepository.save(Seller.from(form));
    }

    //이메일 중복 검증
    public boolean isEmailExist(String email){
        return sellerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .isPresent();

    }

    //인증용 정보
    @Transactional
    public LocalDateTime ChangeSellerValidationEmail(Long sellerId,String verificationCode){
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);

        if (sellerOptional.isPresent()){
            Seller seller = sellerOptional.get();
            seller.setVerificationCode(verificationCode);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return seller.getVerifyExpiredAt();
        }
        //예외처리
        throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }

    //가입 인증처리
    @Transactional
    public void verifyEmail(String email,String code){
        Seller seller = sellerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        if (seller.isVerify()){
            throw new CustomException(ErrorCode.ALREADY_VERIFY);
        } else if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        } else if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRE_CODE);
        }
        seller.setVerify(true);
    }
}
