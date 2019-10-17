package com.chudzick.expanses.scheduler;

import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.factories.CycleStaticFactory;
import com.chudzick.expanses.services.settings.UserSettingsService;
import com.chudzick.expanses.services.transactions.CycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class RenewCycleScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(RenewCycleScheduler.class);

    @Autowired
    private CycleService cycleService;

    @Autowired
    private UserSettingsService userSettingsService;

    @Scheduled(cron = "0 1 0 * * ?")
    public void renewCycles() {
        LOG.info("RenewCycleScheduler invoked");

        List<UserSettings> settings = userSettingsService.findAllUserSettings();

        for (UserSettings setting : settings) {
            Optional<Cycle> oldCycle = cycleService.findActiveCycleByUser(setting.getAppUser());

            if (!oldCycle.isPresent()) {
                continue;
            }
            //1.Check if it is end cycle day
            if (DAYS.between(oldCycle.get().getDateTo(), LocalDate.now()) >= 0) {
                // IF USER HAS AUTOMATIC EXTENSION ON -> RENEW CYCLE ELSE CLOSE ACTIVE AND LEAVE USER WITH NO ACTIVE CYCLE
                if (setting.isAutomaticExtension()) {
                    LOG.info("Renew cycle for user : {}", setting.getAppUser().getLogin());
                    //2. Create new cycle from settings
                    Cycle newCycle = CycleStaticFactory.createRenewalCycle(setting, oldCycle.get());

                    //3. set old setting not active, add new setting
                    cycleService.disableOldCycle(oldCycle.get());
                    cycleService.addRenewalCycle(newCycle, setting.getAppUser());
                } else {
                    cycleService.disableOldCycle(oldCycle.get());
                }

            }

        }
    }

}
