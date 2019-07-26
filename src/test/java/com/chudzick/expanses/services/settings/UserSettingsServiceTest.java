package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.UserSettingsSuplier;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.CycleImpositionException;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.factories.UserSettingsStaticFactory;
import com.chudzick.expanses.repositories.UserSettingsRepository;
import com.chudzick.expanses.services.transactions.CycleService;
import com.chudzick.expanses.services.users.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserSettingsServiceTest implements TestUserSupplier, UserSettingsSuplier {

    private AppUser appUser;

    @InjectMocks
    private UserSettingsServiceImpl userSettingsService;

    @Mock
    private UserService userService;

    @Mock
    private CycleService cycleService;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.appUser = AppUserStaticFactory.createFromDto(prepareValidUserDto());

        when(userService.getCurrentLogInUser()).thenReturn(appUser);
    }


    @Test
    public void addInitialSettingsTest() throws CycleImpositionException {
        UserSettingsDto userSettingsDto = prepareUserSettingsDto(true, true);

        when(userSettingsRepository.findByAppUser(appUser)).thenReturn(Optional.empty());
        when(userSettingsRepository.save(any(UserSettings.class))).thenReturn(UserSettingsStaticFactory.createFromDto(userSettingsDto, appUser));
        when(cycleService.createInitialCycle(any(UserSettings.class), eq(appUser))).thenReturn(null);

        UserSettings userSettings = userSettingsService.saveOrUpdate(userSettingsDto);

        Assert.assertNotNull(userSettings);
        Assert.assertEquals(userSettingsDto.getCycleDays(), userSettings.getCycleDays());
        Assert.assertEquals(userSettingsDto.getCycleSaveGoal(), userSettings.getCycleSaveGoal());
        verify(userSettingsRepository, times(1)).save(any(UserSettings.class));
        verify(cycleService, times(1)).createInitialCycle(any(UserSettings.class), eq(appUser));
    }

    @Test
    public void updateCycleIfExistTest() throws CycleImpositionException {
        UserSettingsDto userSettingsDto = prepareUserSettingsDto(true, true);
        UserSettings userSettings = UserSettingsStaticFactory.createFromDto(userSettingsDto, appUser);

        when(userSettingsRepository.findByAppUser(appUser)).thenReturn(Optional.of(userSettings));
        when(userSettingsRepository.save(any(UserSettings.class))).thenReturn(userSettings);

        UserSettings userSettingsResult = userSettingsService.saveOrUpdate(userSettingsDto);

        Assert.assertNotNull(userSettingsResult);
        Assert.assertEquals(userSettingsDto.getCycleDays(), userSettingsResult.getCycleDays());
        Assert.assertEquals(userSettingsDto.getCycleSaveGoal(), userSettingsResult.getCycleSaveGoal());
        verify(userSettingsRepository, times(1)).save(userSettings);
        verify(cycleService, times(0)).createInitialCycle(any(UserSettings.class), eq(appUser));
    }

    @Test
    public void findByAppUserTest() {

        when(userSettingsRepository.findByAppUser(appUser)).thenReturn(Optional.of(new UserSettings()));

        Optional<UserSettings> result = userSettingsService.findUserSettings();

        Assert.assertTrue(result.isPresent());
        verify(userSettingsRepository,times(1)).findByAppUser(appUser);

    }


}
