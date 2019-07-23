package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;

public interface CycleService {

    Cycle createInitialCycle(UserSettings settingToSave, AppUser currentUser);
}
