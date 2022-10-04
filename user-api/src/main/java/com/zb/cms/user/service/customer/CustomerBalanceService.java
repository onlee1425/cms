package com.zb.cms.user.service.customer;

import com.zb.cms.user.domain.customer.ChangeBalanceForm;
import com.zb.cms.user.domain.model.CustomerBalanceHistory;
import com.zb.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zb.cms.user.domain.repository.CustomerRepository;
import com.zb.cms.user.exception.CustomException;
import com.zb.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    //잔액 변동
    @Transactional(noRollbackFor = {CustomException.class}) //customException발생일 경우 rollback제외
    public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException{
        CustomerBalanceHistory customerBalanceHistory
                = customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
                //값이 없으면
                .orElse(CustomerBalanceHistory.builder()
                        .changeMoney(0)
                        .currentMoney(0)
                        .customer(customerRepository.findById(customerId)
                                //계정이 없다면
                                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
                        .build());
        //현재 잔액 + 가져오는 금액 < 0 일경우
        if (customerBalanceHistory.getCurrentMoney()+ form.getMoney() < 0){
            throw new CustomException(ErrorCode.NOT_ENOUGH_BALANCE);
        }

        //변동처리
        customerBalanceHistory = CustomerBalanceHistory.builder()
                .changeMoney(form.getMoney())
                .currentMoney(customerBalanceHistory.getCurrentMoney()+form.getMoney())
                .description(form.getMessage())
                .fromMessage(form.getFrom())
                .customer(customerBalanceHistory.getCustomer())
                .build();
        customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());


        return customerBalanceHistoryRepository.save(customerBalanceHistory);
    }
}
