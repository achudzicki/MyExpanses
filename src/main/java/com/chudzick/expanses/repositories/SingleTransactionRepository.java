package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SingleTransactionRepository extends JpaRepository<SingleTransaction, Long> {

    List<SingleTransaction> findTop5ByAppUserOrderByIdDesc(AppUser appUser);

    int countSingleTransactioByTransactionGroupId(long groupId);

    List<SingleTransaction> findAllByTransactionGroupId(long groupId);
}
