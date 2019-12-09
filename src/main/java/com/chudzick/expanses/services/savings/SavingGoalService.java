package com.chudzick.expanses.services.savings;

import com.chudzick.expanses.beans.savings.SavingGoalBean;
import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.savings.dto.SavingPaymentDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.util.List;

public interface SavingGoalService {

    List<SavingGoal> findAllUserSavingGoals();

    SavingGoal addNewGoal(SavingGoalDto savingGoalDto);

    SavingGoalBean prepareGoalBean();

    void addPaymentToGoal(Long goalId, SavingPaymentDto savingPaymentDto);

    SavingGoal finById(long goalId);

    void deleteGoalById(Long goalId);

    List<AppUser> findAvailableUsers(long goalId);

    void inviteUser(long goalId, long userId);
}
