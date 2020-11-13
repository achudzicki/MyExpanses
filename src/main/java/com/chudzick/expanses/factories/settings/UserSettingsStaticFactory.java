package com.chudzick.expanses.factories.settings;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;

import java.util.Optional;

public class UserSettingsStaticFactory {

    public static Optional<UserSettings> createFromDto(UserSettingsDto userSettingsDto, AppUser appUser) {
        if (userSettingsDto == null || appUser == null) {
            return Optional.empty();
        }

        UserSettings userSettings = new UserSettings();
        userSettings.setAppUser(appUser);
        userSettings.setCycleDays(userSettingsDto.getCycleDays());
        userSettings.setCycleSaveGoal(userSettingsDto.getCycleSaveGoal());
        userSettings.setAutomaticExtension(userSettingsDto.isAutomaticExtension());
        return Optional.of(userSettings);
    }
}
