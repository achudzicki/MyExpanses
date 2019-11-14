package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.NoActiveCycleException;

import java.util.List;
import java.util.Optional;

public interface CycleService {

    Cycle createInitialCycle(UserSettings settingToSave, AppUser currentUser);

    Optional<Cycle> findActiveCycle();

    Optional<Cycle> findActiveCycleByUser(AppUser appUser);

    void disableOldCycle(Cycle cycle);

    Cycle addRenewalCycle(Cycle newCycle, AppUser appUser);

    List<Cycle> findAllUserCycles();

    Cycle findById(long cycleId) throws NoActiveCycleException;

    long countUserCycles();
}
