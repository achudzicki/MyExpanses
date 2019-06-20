package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.UserDto;

public class AppUserStaticFactory {

    public static AppUser createFromDto(UserDto userDto) {
        AppUser appUser = new AppUser();
        appUser.setEmail(userDto.getEmail());
        appUser.setName(userDto.getName());
        appUser.setGender(userDto.getGender());
        appUser.setLastName(userDto.getLastName());
        appUser.setLogin(userDto.getLogin());
        appUser.setPassword(userDto.getPassword());
        return appUser;
    }
}
