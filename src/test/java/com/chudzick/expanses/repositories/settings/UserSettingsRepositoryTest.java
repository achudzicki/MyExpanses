package com.chudzick.expanses.repositories.settings;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.UserSettingsSuplier;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.factories.settings.UserSettingsStaticFactory;
import com.chudzick.expanses.repositories.UserRepository;
import com.chudzick.expanses.repositories.UserSettingsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserSettingsRepositoryTest implements UserSettingsSuplier, TestUserSupplier {

    private UserSettings userSettings = null;
    private AppUser appUser;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserRepository userRepository;


    @Before
    public void setUp() {
        UserSettingsDto userSettingsDto = prepareUserSettingsDto(true, true);
        UserDto userDto = prepareValidUserDto();

        appUser = AppUserStaticFactory.createFromDto(userDto);
        userSettings = UserSettingsStaticFactory.createFromDto(userSettingsDto, appUser);
    }

    @Test
    public void saveUser() {
        userRepository.save(appUser);
        UserSettings result = userSettingsRepository.save(userSettings);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
    }
}
