package com.chudzick.expanses.services.users;

import com.chudzick.expanses.domain.settings.dto.UserProfileSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<AppUser> findUserByUserName(String userName);

    void register(UserDto appUser) throws LoginAlreadyExistException;

    AppUser getCurrentLogInUser();

    AppUser updateUserProfileInformation(UserProfileSettingsDto userDto, AppUser currentUser);

    AppUser findUserById(long id);

    List<AppUser> findAll();
}
