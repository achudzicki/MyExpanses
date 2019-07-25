package com.chudzick.expanses.factories;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;

import java.time.LocalDate;

public class CycleStaticFactory {
    private static final int CYCLE_MONTH_LENGTH = 1;

    public static Cycle createInitialCycle(UserSettings userSettings, AppUser appUser) {
        LocalDate now = LocalDate.now();
        LocalDate cycleBeginning;

        if (now.getDayOfMonth() < userSettings.getCycleDays()) {
            LocalDate minusMonth = now.minusMonths(1);
            cycleBeginning = LocalDate.of(minusMonth.getYear(), minusMonth.getMonthValue(), userSettings.getCycleDays());
        } else if (now.getDayOfMonth() == userSettings.getCycleDays()) {
            cycleBeginning = now;
        } else {
            cycleBeginning = LocalDate.of(now.getYear(), now.getMonth(), userSettings.getCycleDays());
        }


        LocalDate cycleEnd = cycleBeginning.plusMonths(CYCLE_MONTH_LENGTH);

        Cycle cycle = new Cycle();
        cycle.setActive(true);
        cycle.setDateFrom(cycleBeginning);
        cycle.setDateTo(cycleEnd);
        cycle.setSaveGoal(userSettings.getCycleSaveGoal());
        cycle.setAppUser(appUser);

        return cycle;
    }

    public static Cycle createRenewalCycle(UserSettings userSettings, Cycle oldCycle) {
        LocalDate cycleBegin = oldCycle.getDateTo();
        LocalDate cycleEnd = cycleBegin.plusMonths(CYCLE_MONTH_LENGTH);

        Cycle cycle = new Cycle();
        cycle.setActive(true);
        cycle.setDateFrom(cycleBegin);
        cycle.setDateTo(cycleEnd);
        cycle.setSaveGoal(userSettings.getCycleSaveGoal());
        cycle.setAppUser(userSettings.getAppUser());

        return cycle;
    }
}
