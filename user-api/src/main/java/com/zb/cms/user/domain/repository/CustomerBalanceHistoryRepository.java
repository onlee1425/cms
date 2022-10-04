package com.zb.cms.user.domain.repository;

import com.zb.cms.user.domain.model.CustomerBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface CustomerBalanceHistoryRepository extends JpaRepository<CustomerBalanceHistory,Long> {
    Optional<CustomerBalanceHistory> findFirstByCustomer_IdOrderByIdDesc(@RequestParam("customer_id")Long customerId); //해당 id의 내역중 가장 최신의 데이터를 조회
}
