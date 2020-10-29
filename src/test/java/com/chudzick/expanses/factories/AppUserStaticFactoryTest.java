package com.chudzick.expanses.factories;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import org.junit.Assert;
import org.junit.Test;

public class AppUserStaticFactoryTest implements TestUserSupplier {

    @Test
    public void appUserStaticFactoryCreateFromDtoTest() {
        UserDto userDto = prepareValidUserDto();
        AppUser appUser = AppUserStaticFactory.createFromDto(userDto);

        Assert.assertNotNull(appUser);
        Assert.assertEquals(userDto.getLogin(),appUser.getLogin());
        Assert.assertEquals(userDto.getPassword(),appUser.getPassword());
        Assert.assertEquals(userDto.getName(),appUser.getName());
        Assert.assertEquals(userDto.getLastName(),appUser.getLastName());
        Assert.assertEquals(userDto.getEmail(),appUser.getEmail());
    }
}
