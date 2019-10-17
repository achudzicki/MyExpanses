package com.chudzick.expanses.util.cycle;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class CycleResolver {
    private static final int CYCLE_MONTH_LENGTH = 1;
    private static final int MIN_CYCLE_DURATION = 10;

    public static void setUpInitialCycleDates(UserSettings userSettings, Cycle cycle) {
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

        cycle.setDateFrom(cycleBeginning);
        cycle.setDateTo(cycleEnd);
    }

    public static void setUpRenewalCycleDates(int cycleDays, Cycle oldCycle, Cycle newCycle) {
        LocalDate cycleBegin = oldCycle.getDateTo();
        LocalDate cycleEnd = cycleBegin.plusMonths(CYCLE_MONTH_LENGTH).withDayOfMonth(cycleDays);

        if (DAYS.between(cycleBegin, cycleEnd) < MIN_CYCLE_DURATION) {
            cycleEnd = cycleEnd.plusMonths(1);
        }

        newCycle.setDateFrom(cycleBegin);
        newCycle.setDateTo(cycleEnd);
    }
}
