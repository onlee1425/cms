package com.zb.cms.service;

import com.zb.cms.domain.model.Customer;
import com.zb.cms.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    //token을 통한 id , email 확인
    public Optional<Customer> findByIdAndEmail(Long id,String email){
        return customerRepository.findById(id)
                .stream().filter(customer->customer.getEmail().equals(email))
                .findFirst();
    }

    // 로그인 가능 여부 확인
    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email).stream()
                .filter(customer -> customer.getPassword().equals(password) && customer.isVerify())
                .findFirst();
    }
}
