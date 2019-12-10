package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.savings.SavingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingPaymentRepository extends JpaRepository<SavingPayment, Long> {
}
