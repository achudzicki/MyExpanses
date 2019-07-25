package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.users.UserBean;
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
        return "mainPage";
    }
}
