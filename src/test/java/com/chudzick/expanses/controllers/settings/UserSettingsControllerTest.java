package com.chudzick.expanses.controllers.settings;

import com.chudzick.expanses.UserSettingsSuplier;
import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.UserSettingsStaticFactory;
import com.chudzick.expanses.services.settings.UserSettingsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserSettingsControllerTest implements UserSettingsSuplier {

    private MockMvc mockMvc;
    private AppUser appUser;

    @InjectMocks
    private UserSettingsController userSettingsController;

    @Mock
    private UserSettingsService userSettingsService;

    @Mock
    private NotificationMessagesBean notificationMessagesBean;

    @Before
    public void setUp() {
        AppUser appUser = new AppUser();
        appUser.setLogin("TEST_LOGIN");
        appUser.setLastName("LAST_NAME");
        this.appUser = appUser;

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userSettingsController)
                .build();
    }

    @Test
    public void initUserSettingsPageNewCycle() throws Exception {

        when(userSettingsService.findUserSettings()).thenReturn(Optional.empty());
        doNothing().when(notificationMessagesBean).setNotificationsMessages(anyList());

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userSettingsDto"))
                .andExpect(model().attributeDoesNotExist("userSettings"))
                .andExpect(view().name("settings/userSettingsMainPage"));
    }

    @Test
    public void initUserSettingsPageCycleExist() throws Exception {
        UserSettings userSettings = prepareUserSettings();

        when(userSettingsService.findUserSettings()).thenReturn(Optional.ofNullable(userSettings));
        doNothing().when(notificationMessagesBean).setNotificationsMessages(anyList());

        MvcResult result = mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userSettingsDto"))
                .andExpect(model().attributeExists("userSettings"))
                .andExpect(view().name("settings/userSettingsMainPage"))
                .andReturn();

        UserSettings modelSettings = (UserSettings) result.getModelAndView().getModel().get("userSettings");

        Assert.assertNotNull(modelSettings);
        Assert.assertEquals(modelSettings.getAppUser(), appUser);
        Assert.assertEquals(modelSettings.getCycleDays(), UserSettingsSuplier.VALID_TEST_DAY_STAT_CYCLE);
        Assert.assertEquals(modelSettings.getCycleSaveGoal(), BigDecimal.valueOf(UserSettingsSuplier.VALID_TEST_SAVE_GOAL));
        Assert.assertEquals(modelSettings.isAutomaticExtension(), UserSettingsSuplier.TEST_AUTOMATIC_EXTENSION);
    }

    @Test
    public void setUpUserCycleValidTest() throws Exception {
        UserSettingsDto userSettingsDto = prepareUserSettingsDto(true, true);
        UserSettings userSettings = UserSettingsStaticFactory.createFromDto(userSettingsDto, appUser);

        when(userSettingsService.saveOrUpdate(userSettingsDto)).thenReturn(userSettings);

        mockMvc.perform(post("/settings/setup/cycle")
                .flashAttr("userSettingsDto", userSettingsDto))
                .andExpect(status().is3xxRedirection());

        verify(userSettingsService, times(1)).saveOrUpdate(userSettingsDto);
    }

    @Test
    public void setUpCycleNotValidDaysTest() throws Exception {
        sendAndCheckPostWithNotValidFields(false, true);
    }

    @Test
    public void setUpCycleNotValidSaveGoalTest() throws Exception {
        sendAndCheckPostWithNotValidFields(true, false);
    }

    private void sendAndCheckPostWithNotValidFields(boolean validDays, boolean validSaveGoal) throws Exception {
        UserSettingsDto userSettingsDto = prepareUserSettingsDto(validDays, validSaveGoal);
        UserSettings userSettings = UserSettingsStaticFactory.createFromDto(userSettingsDto, appUser);

        when(userSettingsService.findUserSettings()).thenReturn(Optional.ofNullable(userSettings));

        mockMvc.perform(post("/settings/setup/cycle")
                .flashAttr("userSettingsDto", userSettingsDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notificationMessagesBean"))
                .andExpect(model().attributeExists("userSettings"))
                .andExpect(view().name("settings/userSettingsMainPage"));
    }

    private UserSettings prepareUserSettings() {
        UserSettings userSettings = new UserSettings();

        userSettings.setAutomaticExtension(UserSettingsSuplier.TEST_AUTOMATIC_EXTENSION);
        userSettings.setCycleDays(UserSettingsSuplier.VALID_TEST_DAY_STAT_CYCLE);
        userSettings.setCycleSaveGoal(new BigDecimal(UserSettingsSuplier.VALID_TEST_SAVE_GOAL));
        userSettings.setAppUser(appUser);

        return userSettings;
    }

}
