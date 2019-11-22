package com.chudzick.expanses.services.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;

import java.util.List;

public interface SavingGoalService {

    List<SavingGoal> findAllUserSavingGoals();

    SavingGoal addNewGoal(SavingGoalDto savingGoalDto);
}
