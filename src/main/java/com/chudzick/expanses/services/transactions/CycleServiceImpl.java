package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.CycleStaticFactory;
import com.chudzick.expanses.repositories.CycleRepository;
import com.chudzick.expanses.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CycleServiceImpl implements CycleService {

    private static final Logger LOG = LoggerFactory.getLogger(CycleServiceImpl.class);
    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Cycle createInitialCycle(UserSettings userSettings, AppUser currentUser) {
        Cycle cycleToSave = CycleStaticFactory.createInitialCycle(userSettings, currentUser);
        return cycleRepository.save(cycleToSave);
    }

    @Override
    public Optional<Cycle> findActiveCycle() {
        AppUser appUser = userService.getCurrentLogInUser();
        return cycleRepository.findByAppUserAndActive(appUser, true);
    }

    @Override
    public Optional<Cycle> findActiveCycleByAppUser(AppUser appUser) {
        return cycleRepository.findByAppUserAndActive(appUser, true);
    }

    @Override
    public void disableOldCycle(Cycle cycle) {
        cycle.setActive(false);
        cycleRepository.save(cycle);
    }

    @Override
    public Cycle addRenewalCycle(Cycle newCycle) {
        return cycleRepository.save(newCycle);
    }
}
