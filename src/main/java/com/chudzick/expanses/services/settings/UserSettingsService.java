package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.exceptions.CycleImpositionException;

import java.util.List;
import java.util.Optional;

public interface UserSettingsService {

    UserSettings saveOrUpdate(UserSettingsDto userSettingsDto) throws CycleImpositionException;

    Optional<UserSettings> findUserSettings();

    List<UserSettings> findAllUserSettings();
}
