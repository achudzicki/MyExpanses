package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.savings.SavingGoalRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingGoalRequestRepository extends JpaRepository<SavingGoalRequest,Long> {
}
