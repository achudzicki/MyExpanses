package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.UserSettingsStaticFactory;
import com.chudzick.expanses.repositories.UserSettingsRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserSettings saveOrUpdate(UserSettingsDto userSettingsDto) {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<UserSettings> userSettings = userSettingsRepository.findByAppUser(currentUser);
        UserSettings settingToSave;

        if (userSettings.isPresent()) {
            settingToSave = update(userSettings.get(), userSettingsDto);
        } else {
            settingToSave = UserSettingsStaticFactory.createFromDto(userSettingsDto, currentUser);
        }
        return userSettingsRepository.save(settingToSave);
    }

    @Override
    public Optional<UserSettings> findUserSettings() {
        AppUser currentUser = userService.getCurrentLogInUser();
        return userSettingsRepository.findByAppUser(currentUser);
    }

    private UserSettings update(UserSettings userSettings, UserSettingsDto userSettingsDto) {
        userSettings.setCycleSaveGoal(userSettingsDto.getCycleSaveGoal());
        userSettings.setCycleDays(userSettingsDto.getCycleDays());
        userSettings.setAutomaticExtension(userSettingsDto.isAutomaticExtension());
        return userSettings;
    }
}
