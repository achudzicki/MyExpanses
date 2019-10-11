package com.chudzick.expanses;

import com.chudzick.expanses.repositories.CycleRepository;
import com.chudzick.expanses.repositories.SingleTransactionRepository;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.scheduler.RenewCycleScheduler;
import com.chudzick.expanses.services.users.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private SingleTransactionRepository singleTransactionRepository;

    @Autowired
    private TransactionGroupRepository transactionGroupRepository;

    @Override
    public void run(String... args) throws Exception {
        renewCycleScheduler.renewCycles();

 /*       LocalDate localDate = LocalDate.of(2019, 1, 28);

        Optional<AppUser> appUser = userService.findUserByUserName("admin1");
        TransactionGroup transactionGroup = new TransactionGroup();
        transactionGroup.setAppUser(appUser.get());
        transactionGroup.setGroupDescription("sadasd");
        transactionGroup.setGorupName("TESTOWA_AAAA");
        TransactionGroup savedTransactionGroup = transactionGroupRepository.save(transactionGroup);

        for (int i = 0; i < 5; i++) {
            Cycle cycle = new Cycle();
            cycle.setActive(false);
            cycle.setAppUser(appUser.get());
            cycle.setDateFrom(localDate);
            cycle.setDateTo(localDate.plusMonths(1));
            cycle.setSaveGoal(BigDecimal.valueOf(1000));
            Cycle saved = cycleRepository.save(cycle);

            Random random = new Random();
            for (int j = 0; j < 10; j++) {
                SingleTransaction singleTransaction = new SingleTransaction();
                singleTransaction.setCycle(saved);

                long randomDay = ThreadLocalRandom.current().nextLong(cycle.getDateFrom().toEpochDay(), cycle.getDateTo().toEpochDay());
                singleTransaction.setTransactionDate(LocalDate.ofEpochDay(randomDay));
                double randomVal = random.nextDouble() * 1000;
                singleTransaction.setAmount(BigDecimal.valueOf(randomVal));
                singleTransaction.setAppUser(appUser.get());
                singleTransaction.setTransactionDuration(TransactionDuration.SINGLE);
                singleTransaction.setTransactionType(TransactionType.EXPANSE);
                singleTransaction.setTransactionGroup(savedTransactionGroup);
                singleTransactionRepository.save(singleTransaction);
            }

            localDate = localDate.plusMonths(1);
        }*/
    }
}
