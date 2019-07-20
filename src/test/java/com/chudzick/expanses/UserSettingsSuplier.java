package com.chudzick.expanses;

import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;

import java.math.BigDecimal;

public interface UserSettingsSuplier {
    int VALID_TEST_SAVE_GOAL = 2;
    int INVALID_TEST_SAVE_GOAL = -1;
    int VALID_TEST_DAY_STAT_CYCLE = 28;
    int INVALID_TEST_DAY_STAT_CYCLE = 30;
    boolean TEST_AUTOMATIC_EXTENSION = true;

    default UserSettingsDto prepareUserSettingsDto(boolean validDays, boolean validSaveGoal) {
        UserSettingsDto userSettingsDto = new UserSettingsDto();
        if (validDays) {
            userSettingsDto.setCycleDays(VALID_TEST_DAY_STAT_CYCLE);
        } else {
            userSettingsDto.setCycleDays(INVALID_TEST_DAY_STAT_CYCLE);
        }
        if (validSaveGoal) {
            userSettingsDto.setCycleSaveGoal(new BigDecimal(VALID_TEST_SAVE_GOAL));
        } else {
            userSettingsDto.setCycleSaveGoal(new BigDecimal(INVALID_TEST_SAVE_GOAL));
        }
        userSettingsDto.setAutomaticExtension(TEST_AUTOMATIC_EXTENSION);
        return userSettingsDto;
    }
}
