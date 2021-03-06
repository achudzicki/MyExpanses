package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @GetMapping(value = "/register")
    public String registration(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registration(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "register";
        }
        try {
            userService.register(userDto);
        } catch (LoginAlreadyExistException ex) {
            model.addAttribute("userExistError", ex.getMessage());
            return "register";
        }

        notificationMessagesBean.setNotificationsMessages(
                new NotificationMessageListBuilder()
                        .withSuccessNotification("Zarejestrowano użytkownika")
                        .getNotificationList()
        );

        model.addAttribute("notificationMessagesBean",notificationMessagesBean);
        return "login";
    }
}
