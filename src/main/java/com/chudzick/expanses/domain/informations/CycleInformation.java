package com.chudzick.expanses.domain.informations;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.util.PolishMothNameResolver;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public final class CycleInformation {

    private final String cycleDisplayName;
    private final long allCycleDays;
    private final long currentCycleDay;

    private CycleInformation(String cycleDisplayName, long allCycleDays, long currentCycleDay) {
        this.cycleDisplayName = cycleDisplayName;
        this.allCycleDays = allCycleDays;
        this.currentCycleDay = currentCycleDay;
    }

    public static CycleInformation fromCycle(Cycle cycle) {
        return new CycleInformation(
                PolishMothNameResolver.getMonthName(cycle.getDateFrom().getMonthValue()) + ", " + cycle.getDateFrom().getYear(),
                DAYS.between(cycle.getDateFrom(), cycle.getDateTo()),
                DAYS.between(cycle.getDateFrom(), LocalDate.now())
        );
    }

    public String getCycleDisplayName() {
        return cycleDisplayName;
    }

    public long getAllCycleDays() {
        return allCycleDays;
    }

    public long getCurrentCycleDay() {
        return currentCycleDay;
    }
}
