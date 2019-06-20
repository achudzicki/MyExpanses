package com.chudzick.expanses.mappers;

import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.UserDto;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToAppUserMapper implements ObjectMapper<AppUser, UserDto> {

    @Override
    public AppUser mapObjects(UserDto oldObject) {
        return AppUserStaticFactory.createFromDto(oldObject);
    }
}
