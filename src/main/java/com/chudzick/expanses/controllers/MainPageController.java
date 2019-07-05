package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.UserBean;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        AppUser currentLogInUser = userService.getCurrentLogInUser();

        userBean.setAppUser(currentLogInUser);

        model.addAttribute("loggedUser", userBean.getAppUser());
        initTestDat();
        return "home";
    }


    // TODO do usuniecia
    private void initTestDat() {
        TransactionGroup transactionGroup = new TransactionGroup();

        transactionGroup.setAppUser(userService.getCurrentLogInUser());
        transactionGroup.setGorupName("TEST_GROUP");
        transactionGroup.setGroupDescription("TEST_GROUP");
        transactionGroupRepository.save(transactionGroup);
    }
}
