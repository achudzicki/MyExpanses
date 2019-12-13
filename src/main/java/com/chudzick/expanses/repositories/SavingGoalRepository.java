package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingGoalRepository extends JpaRepository<SavingGoal,Long> {

    List<SavingGoal> findAllByAppUsers(AppUser appUser);
}
