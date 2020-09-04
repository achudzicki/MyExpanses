package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
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
    public ResponseEntity<AppUser> registration(@RequestBody UserDto userDto) {
        AppUser appUser;

        try {
            appUser = userService.register(userDto);
        } catch (LoginAlreadyExistException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Login already Exist");
        }

        return new ResponseEntity<>(appUser, HttpStatus.OK);
    }
}
