package com.chudzick.expanses.repositories.transactionsApp;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SingleTransactionRepository extends JpaRepository<SingleTransaction,Long> {
}
