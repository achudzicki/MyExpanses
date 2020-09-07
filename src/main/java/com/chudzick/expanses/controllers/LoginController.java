package com.chudzick.expanses.controllers;

import com.chudzick.expanses.domain.api.Response;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.mappers.UserDtoToAppUserMapper;
import com.chudzick.expanses.services.users.UserService;
import com.chudzick.expanses.validators.register.PasswordMatches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoToAppUserMapper userMapper;

    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseEntity<Response<UserDto>> registration(@Valid @PasswordMatches @RequestBody UserDto userDto) {
        AppUser appUser = userService.register(userDto);
        Response<UserDto> response = new Response<>(userMapper.reverseMapping(appUser));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
