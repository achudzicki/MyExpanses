package com.chudzick.expanses.repositories.transactionsApp;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionGroupRepository extends JpaRepository<TransactionGroup,Long> {
}
