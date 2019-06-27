package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.UserBean;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.LoggedInUserNotFoundException;
import com.chudzick.expanses.repositories.transactionsApp.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MainPageController {

    @Autowired
    private UserBean userBean;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @GetMapping(value = "/")
    public String initMainPage(Model model) {

        Optional<AppUser> currentLogInUser = userService.getCurrentLogInUser();
        if (currentLogInUser.isPresent()) {
            userBean.setAppUser(currentLogInUser.get());
        } else {
            throw new LoggedInUserNotFoundException();
        }

        model.addAttribute("loggedUser", userBean.getAppUser());
        initTestDat();
        return "home";
    }


    // TODO do usuniecia
    private void initTestDat() {
        TransactionGroup transactionGroup = new TransactionGroup();

        transactionGroup.setAppUser(userService.getCurrentLogInUser().get());
        transactionGroup.setGorupName("TEST_GROUP");
        transactionGroup.setGroupDescription("TEST_GROUP");
        transactionGroupRepository.save(transactionGroup);
    }
}
