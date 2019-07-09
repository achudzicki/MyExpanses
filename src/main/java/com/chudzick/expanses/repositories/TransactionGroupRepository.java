package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionGroupRepository extends JpaRepository<TransactionGroup,Long> {
}
