package com.chudzick.expanses.beans.notifications;

import com.chudzick.expanses.domain.savings.SavingGoalRequest;
import com.chudzick.expanses.services.savings.SavingGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MainPageNotificationBean {

    @Autowired
    private SavingGoalService savingGoalService;
    private List<SavingGoalRequest> userSavingGoalRequests;

    @PostConstruct
    public void initRequests() {
      userSavingGoalRequests = savingGoalService.findUserSavingGoalRequests();
    }

    public void resetBean() {
        userSavingGoalRequests = savingGoalService.findUserSavingGoalRequests();
    }

    public List<SavingGoalRequest> getUserSavingGoalRequests() {
        return userSavingGoalRequests;
    }

    public void setUserSavingGoalRequests(List<SavingGoalRequest> userSavingGoalRequests) {
        this.userSavingGoalRequests = userSavingGoalRequests;
    }
}
