package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;

import java.util.Optional;

public interface UserSettingsService {

    UserSettings saveOrUpdate(UserSettingsDto userSettingsDto);

    Optional<UserSettings> findUserSettings();
}
