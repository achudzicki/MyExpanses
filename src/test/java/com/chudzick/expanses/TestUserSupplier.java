package com.chudzick.expanses;

import com.chudzick.expanses.domain.UserDto;

public interface TestUserSupplier {

    default UserDto prepareValidUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testLogin");
        userDto.setPassword("testPassword1!");
        userDto.setRepeatedPassword("testPassword1!");
        userDto.setName("firstName");
        userDto.setLastName("lastName");
        userDto.setEmail("validEmail@email.com");
        userDto.setGender("man");
        return userDto;
    }
}
