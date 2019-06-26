package com.chudzick.expanses.mappers;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.UserDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDtoToAppUserMapperTest implements TestUserSupplier {

    @Autowired
    private UserDtoToAppUserMapper userDtoToAppUserMapper;


    @Test
    public void setUserDtoToAppUserMapperNotNull() {
        Assert.assertNotNull(userDtoToAppUserMapper);
    }
    @Test
    public void mapDtoToAppUserTest() {
        UserDto userDto = prepareValidUserDto();
        AppUser appUser = userDtoToAppUserMapper.mapObjects(userDto);

        Assert.assertNotNull(appUser);
        Assert.assertEquals(userDto.getLogin(), appUser.getLogin());
        Assert.assertEquals(userDto.getPassword(), appUser.getPassword());
        Assert.assertEquals(userDto.getName(), appUser.getName());
        Assert.assertEquals(userDto.getLastName(), appUser.getLastName());
        Assert.assertEquals(userDto.getEmail(), appUser.getEmail());
    }
}
