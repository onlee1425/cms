package com.zb.cms.user.service.seller;

import com.zb.cms.user.domain.model.Seller;
import com.zb.cms.user.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public Optional<Seller> findByIdAndEmail(Long id, String email){
        return sellerRepository.findByIdAndEmail(id,email);
    }
    public Optional<Seller> findValidSeller(String email,String password){
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email,password);
    }
}
