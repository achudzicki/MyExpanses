package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;

public class UserSettingsStaticFactory {

    public static UserSettings createFromDto(UserSettingsDto userSettingsDto, AppUser appUser) {
        UserSettings userSettings = new UserSettings();

        userSettings.setAppUser(appUser);
        userSettings.setCycleDays(userSettingsDto.getCycleDays());
        userSettings.setCycleSaveGoal(userSettingsDto.getCycleSaveGoal());
        userSettings.setAutomaticExtension(userSettingsDto.isAutomaticExtension());

        return userSettings;
    }
}
