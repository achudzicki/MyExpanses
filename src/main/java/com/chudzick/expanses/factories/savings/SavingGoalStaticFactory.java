package com.chudzick.expanses.factories.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.util.HashSet;
import java.util.Set;

public class SavingGoalStaticFactory {


    public static SavingGoal createNewFromDto(SavingGoalDto savingGoalDto, AppUser creator) {
        SavingGoal savingGoal = new SavingGoal();
        Set<AppUser> initialUser = new HashSet<>();
        initialUser.add(creator);

        savingGoal.setDateFrom(savingGoalDto.getDateFrom());
        savingGoal.setDateTo(savingGoalDto.getDateTo());
        savingGoal.setGoalName(savingGoalDto.getName());
        savingGoal.setAppUsers(initialUser);
        savingGoal.setStartAmount(savingGoalDto.getInitialAmount());
        savingGoal.setGoal(savingGoalDto.getTotalAmount());

        return savingGoal;
    }
}
