package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.util.cycle.CycleResolver;

public class CycleStaticFactory {

    public static Cycle createInitialCycle(UserSettings userSettings, AppUser appUser) {
        Cycle cycle = new Cycle();
        cycle.setActive(true);
        cycle.setSaveGoal(userSettings.getCycleSaveGoal());
        cycle.setAppUser(appUser);
        CycleResolver.setUpInitialCycleDates(userSettings, cycle);
        return cycle;
    }

    public static Cycle createRenewalCycle(UserSettings userSettings, Cycle oldCycle) {
        Cycle cycle = new Cycle();
        cycle.setActive(true);
        cycle.setSaveGoal(userSettings.getCycleSaveGoal());
        cycle.setAppUser(userSettings.getAppUser());
        CycleResolver.setUpRenewalCycleDates(userSettings.getCycleDays(), oldCycle, cycle);
        return cycle;
    }

    public static Cycle createNewCyclePreview(UserSettingsDto userSettingsDto, Cycle currentActiveCycle) {
        Cycle cycle = new Cycle();
        CycleResolver.setUpRenewalCycleDates(userSettingsDto.getCycleDays(), currentActiveCycle, cycle);
        return cycle;
    }
}
