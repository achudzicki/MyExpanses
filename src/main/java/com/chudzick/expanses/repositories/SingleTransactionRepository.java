package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SingleTransactionRepository extends JpaRepository<SingleTransaction, Long> {

    List<SingleTransaction> findTop5ByAppUserAndCycleOrderByIdDesc(AppUser appUser, Cycle cycle);

    int countSingleTransactionByTransactionGroupId(long groupId);

    List<SingleTransaction> findAllByTransactionGroupId(long groupId);

    List<SingleTransaction> findAllByAppUserAndCycleOrderByIdDesc(AppUser appUser, Cycle cycle);

    Optional<SingleTransaction> findById(long id);

    Page<SingleTransaction> findAll(Pageable pageRequest);
}
