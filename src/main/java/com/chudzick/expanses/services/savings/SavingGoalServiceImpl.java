package com.chudzick.expanses.services.savings;

import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.savings.SavingGoalStaticFactory;
import com.chudzick.expanses.repositories.SavingGoalRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SavingGoalServiceImpl implements SavingGoalService {

    @Autowired
    private SavingGoalRepository savingGoalRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<SavingGoal> findAllUserSavingGoals() {
        AppUser appUser = userService.getCurrentLogInUser();
        return savingGoalRepository.findAllByAppUsers(appUser);
    }

    @Override
    public SavingGoal addNewGoal(SavingGoalDto savingGoalDto) {
        AppUser appUser = userService.getCurrentLogInUser();
        SavingGoal savingGoal = SavingGoalStaticFactory.createNewFromDto(savingGoalDto,appUser);
        return savingGoalRepository.save(savingGoal);
    }
}
