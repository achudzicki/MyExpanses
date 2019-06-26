package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.UserBean;
import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.exceptions.LoggedInUserNotFoundException;
import com.chudzick.expanses.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class MianPageController {

    @Autowired
    private UserBean userBean;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String initMainPage(Model model) {

        Optional<AppUser> currentLogInUser = userService.getCurrentLogInUser();
        if (currentLogInUser.isPresent()) {
            userBean.setAppUser(currentLogInUser.get());
        } else {
            throw new LoggedInUserNotFoundException("Could not find current logged in user");
        }

        model.addAttribute("loggedUser", userBean.getAppUser());
        return "home";
    }

}
