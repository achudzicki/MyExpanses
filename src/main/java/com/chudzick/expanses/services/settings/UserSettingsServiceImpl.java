package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.CycleImpositionException;
import com.chudzick.expanses.factories.UserSettingsStaticFactory;
import com.chudzick.expanses.repositories.UserSettingsRepository;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.users.UserService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CycleService cycleService;

    @Override
    @Transactional
    public UserSettings saveOrUpdate(UserSettingsDto userSettingsDto) throws CycleImpositionException {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<UserSettings> userSettings = userSettingsRepository.findByAppUser(currentUser);
        UserSettings settingToSave;

        if (userSettings.isPresent()) {
            Optional<Cycle> activeCycle = cycleService.findActiveCycle();
            if (activeCycle.isPresent()) {
                throw new CycleImpositionException();
            } else {
                settingToSave = update(userSettings.get(), userSettingsDto);
            }
        } else {
            settingToSave = UserSettingsStaticFactory.createFromDto(userSettingsDto, currentUser);
            cycleService.createInitialCycle(settingToSave, currentUser);
        }
        return userSettingsRepository.save(settingToSave);
    }

    @Override
    public Optional<UserSettings> findUserSettings() {
        AppUser currentUser = userService.getCurrentLogInUser();
        return userSettingsRepository.findByAppUser(currentUser);
    }

    @Override
    public List<UserSettings> findAllUserSettings() {
        return userSettingsRepository.findAll();
    }

    private UserSettings update(UserSettings userSettings, UserSettingsDto userSettingsDto) {
        userSettings.setCycleSaveGoal(userSettingsDto.getCycleSaveGoal());
        userSettings.setCycleDays(userSettingsDto.getCycleDays());
        userSettings.setAutomaticExtension(userSettingsDto.isAutomaticExtension());
        return userSettings;
    }
}
