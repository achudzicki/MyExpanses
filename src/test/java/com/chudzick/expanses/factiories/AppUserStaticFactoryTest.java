package com.chudzick.expanses.factiories;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.UserDto;
import com.chudzick.expanses.factories.AppUserStaticFactory;
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
