package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.categorize.TransactionGroupCategorizeData;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategorizeRepository extends JpaRepository<TransactionGroupCategorizeData, Long> {

    Optional<TransactionGroupCategorizeData> findByTransactionGroupAndKey(TransactionGroup transactionGroup, String key);

    List<TransactionGroupCategorizeData> findAllByAppUser(AppUser appUser);
}
