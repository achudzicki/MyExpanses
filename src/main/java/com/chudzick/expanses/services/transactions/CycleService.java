package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;

import java.util.Optional;

public interface CycleService {

    Cycle createInitialCycle(UserSettings settingToSave, AppUser currentUser);

    Optional<Cycle> findActiveCycle();

    Optional<Cycle> findActiveCycleByUser(AppUser appUser);

    void disableOldCycle(Cycle cycle);

    Cycle addRenewalCycle(Cycle newCycle, AppUser appUser);
}
