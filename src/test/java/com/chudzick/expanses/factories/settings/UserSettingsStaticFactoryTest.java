package com.chudzick.expanses.factories.settings;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;

public class UserSettingsStaticFactoryTest {
    private static final AppUser USER = new AppUser();
    private static final BigDecimal CYCLE_SAVE_GOAL = new BigDecimal(1);
    private static final int CYCLE_DAYS = 10;
    private static final boolean AUTOMATIC_EXTENSION = true;

    @Test
    public void createFromDto_PassFullData_ReturnOptionalUserSettings() {
        UserSettingsDto dto = prepareDto();

        Optional<UserSettings> userSettings = UserSettingsStaticFactory.createFromDto(dto, USER);

        Assert.assertTrue(userSettings.isPresent());
        Assert.assertEquals(CYCLE_SAVE_GOAL, userSettings.get().getCycleSaveGoal());
        Assert.assertEquals(CYCLE_DAYS, userSettings.get().getCycleDays());
        Assert.assertEquals(AUTOMATIC_EXTENSION, userSettings.get().isAutomaticExtension());
        Assert.assertEquals(USER, userSettings.get().getAppUser());
    }

    @Test
    public void createFromDto_PassNullAppUser_ReturnOptionalUserSettings() {
        UserSettingsDto dto = prepareDto();

        Optional<UserSettings> userSettings = UserSettingsStaticFactory.createFromDto(dto, null);

        Assert.assertFalse(userSettings.isPresent());
    }


    @Test
    public void createFromDto_PassNullDto_ReturnOptionalUserSettings() {
        Optional<UserSettings> userSettings = UserSettingsStaticFactory.createFromDto(null, USER);

        Assert.assertFalse(userSettings.isPresent());
    }

    private UserSettingsDto prepareDto() {
        UserSettingsDto dto = new UserSettingsDto();
        dto.setCycleSaveGoal(CYCLE_SAVE_GOAL);
        dto.setCycleDays(CYCLE_DAYS);
        dto.setAutomaticExtension(AUTOMATIC_EXTENSION);
        return dto;
    }
}