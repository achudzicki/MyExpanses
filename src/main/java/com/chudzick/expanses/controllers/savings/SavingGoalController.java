package com.chudzick.expanses.controllers.savings;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.savings.SavingGoal;
import com.chudzick.expanses.domain.savings.dto.SavingGoalDto;
import com.chudzick.expanses.services.savings.SavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "savings")
public class SavingGoalController {

    @Autowired
    private SavingGoalService savingGoalService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;


    @GetMapping(value = "goal")
    public String getAllUserSavingGoals(Model model) {
        return prepareSavingGoalPage(model);
    }


    @PostMapping("goal/add")
    public String addSavingGoal(@ModelAttribute @Valid SavingGoalDto savingGoalDto, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Błędne dane, błąd podczas zapisu celu")
                    .getNotificationList());
            model.addAttribute("notificationMessagesBean", notificationMessagesBean);
            model.addAttribute("bindingResult", bindingResult);
            return prepareSavingGoalPage(model);
        }
        savingGoalService.addNewGoal(savingGoalDto);
        return prepareSavingGoalPage(model);
    }

    private String prepareSavingGoalPage(Model model) {
        List<SavingGoal> userSavingGoals = savingGoalService.findAllUserSavingGoals();

        model.addAttribute("userSavingGoals", userSavingGoals);
        model.addAttribute("savingGoalDto", new SavingGoalDto());
        return "savings/savingGoals";
    }

}
