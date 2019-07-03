package com.chudzick.expanses.services.users;

import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;

import java.util.Optional;

public interface UserService {
    Optional<AppUser> findUserByUserName(String userName);

    void register(UserDto appUser) throws LoginAlreadyExistException;

    Optional<AppUser> getCurrentLogInUser();
}
