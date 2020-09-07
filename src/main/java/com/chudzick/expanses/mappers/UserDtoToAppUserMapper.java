package com.chudzick.expanses.mappers;

import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToAppUserMapper implements ObjectMapper<AppUser, UserDto> {

    @Override
    public AppUser mapObjects(UserDto oldObject) {
        return AppUserStaticFactory.createFromDto(oldObject);
    }

    @Override
    public UserDto reverseMapping(AppUser oldObject) {
        UserDto userDto = new UserDto();
        userDto.setEmail(oldObject.getEmail());
        userDto.setGender(oldObject.getGender());
        userDto.setLastName(oldObject.getLastName());
        userDto.setLogin(oldObject.getLogin());
        userDto.setName(oldObject.getName());
        return userDto;
    }
}
