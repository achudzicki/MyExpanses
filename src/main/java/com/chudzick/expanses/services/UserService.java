package com.chudzick.expanses.services;

import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;

import java.util.Optional;

public interface UserService {
    Optional<AppUser> findUserByUserName(String userName);

    void register(UserDto appUser) throws LoginAlreadyExistException;
}
