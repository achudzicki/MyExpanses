package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SingleTransactionRepository extends JpaRepository<SingleTransaction, Long> {

    List<SingleTransaction> findTop5ByAppUserOrderByIdDesc(AppUser appUser);

    int countSingleTransactionByTransactionGroupId(long groupId);

    List<SingleTransaction> findAllByTransactionGroupId(long groupId);

    List<SingleTransaction> findAllByAppUser(AppUser appUser);

    Optional<SingleTransaction> findById(long id);
}
