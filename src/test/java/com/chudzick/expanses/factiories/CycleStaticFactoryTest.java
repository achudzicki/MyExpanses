package com.chudzick.expanses.factiories;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.factories.CycleStaticFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

public class CycleStaticFactoryTest implements TestUserSupplier {

    private AppUser appUser;
    private static final BigDecimal TEST_SAVE_GOAL = BigDecimal.valueOf(2700);

    @Before
    public void setUp() {
        appUser = AppUserStaticFactory.createFromDto(prepareValidUserDto());
    }

    @Test
    public void createInitialCycleTest() {
        UserSettings settings = prepareUserSettings();

        Cycle cycle = CycleStaticFactory.createInitialCycle(settings, appUser);

        Assert.assertEquals(cycle.getDateFrom().getDayOfMonth(), settings.getCycleDays());
        Assert.assertEquals(cycle.getDateTo().getDayOfMonth(), settings.getCycleDays());
        Assert.assertEquals(cycle.getAppUser(), appUser);
        Assert.assertEquals(cycle.getSaveGoal(), settings.getCycleSaveGoal());
    }

    @Test
    public void createRenewalCycleTest() {
        Cycle oldCycle = new Cycle();
        oldCycle.setDateFrom(LocalDate.now().minusDays(5));
        oldCycle.setDateTo(oldCycle.getDateFrom().plusMonths(1));
        oldCycle.setSaveGoal(TEST_SAVE_GOAL);
        oldCycle.setAppUser(appUser);
        oldCycle.setCycleTransactions(Collections.emptyList());
        UserSettings settings = prepareUserSettings();

        Cycle newCycle = CycleStaticFactory.createRenewalCycle(settings,oldCycle);

        Assert.assertEquals(newCycle.getSaveGoal(),oldCycle.getSaveGoal());
        Assert.assertEquals(newCycle.getDateFrom(),oldCycle.getDateTo());
        Assert.assertEquals(newCycle.getDateTo(),oldCycle.getDateTo().plusMonths(1).withDayOfMonth(settings.getCycleDays()));
        Assert.assertEquals(newCycle.getAppUser(),oldCycle.getAppUser());
    }

    private UserSettings prepareUserSettings() {
        UserSettings settings = new UserSettings();
        settings.setAutomaticExtension(true);
        settings.setCycleDays(10);
        settings.setCycleSaveGoal(TEST_SAVE_GOAL);
        settings.setAppUser(appUser);
        return settings;
    }

}
