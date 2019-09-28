package com.chudzick.expanses;

import com.chudzick.expanses.scheduler.RenewCycleScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpansesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExpansesApplication.class, args);
    }

    @Autowired
    private RenewCycleScheduler renewCycleScheduler;

    @Override
    public void run(String... args) throws Exception {
        renewCycleScheduler.renewCycles();
    }
}
