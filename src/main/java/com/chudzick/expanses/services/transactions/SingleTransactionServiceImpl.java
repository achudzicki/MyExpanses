package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.dto.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.SingleTransactionStaticFactory;
import com.chudzick.expanses.repositories.SingleTransactionRepository;
import com.chudzick.expanses.services.permissions.PermissionsService;
import com.chudzick.expanses.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SingleTransactionServiceImpl implements SingleTransactionService<SingleTransaction, SingleTransactionDto> {

    private static final Logger LOG = LoggerFactory.getLogger(SingleTransactionServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SingleTransactionRepository singleTransactionRepository;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private PermissionsService permissionsService;

    @Override
    public List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit) {
        AppUser current = userService.getCurrentLogInUser();
        Optional<Cycle> currentCycle = cycleService.findActiveCycle();

        if (!currentCycle.isPresent()) {
            return Collections.emptyList();
        }

        return singleTransactionRepository.findTop5ByAppUserAndCycleOrderByIdDesc(current, currentCycle.get());
    }

    @Override
    public List<SingleTransaction> findFilteredTransactions(Specification<SingleTransaction> specification) {
        return singleTransactionRepository.findAll(specification);
    }

    @Override
    public SingleTransaction addNewTransaction(SingleTransactionDto dto) throws NoActiveCycleException {
        AppUser appUser = userService.getCurrentLogInUser();
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            LOG.warn("Try to add new transaction with no active cycle");
            throw new NoActiveCycleException(ApplicationActions.ADD_TRANSACTION);
        }

        SingleTransaction newTransaction = SingleTransactionStaticFactory.createFromDto(dto, appUser, activeCycle.get());
        return singleTransactionRepository.save(newTransaction);
    }

    @Override
    public int countTransactionsByGroup(long groupId) {
        return singleTransactionRepository.countSingleTransactionByTransactionGroupId(groupId);
    }

    @Override
    public List<SingleTransaction> findAllTransactionsByGroupId(long groupId) {
        return singleTransactionRepository.findAllByTransactionGroupId(groupId);
    }

    @Override
    public boolean deleteTransactionById(long transactionId) throws UserNotPermittedToActionException {
        Optional<SingleTransaction> singleTransaction = singleTransactionRepository.findById(transactionId);

        if (!singleTransaction.isPresent()) {
            return false;
        }

        permissionsService.checkPermissionToDeleteTransaction(singleTransaction.get());
        singleTransactionRepository.delete(singleTransaction.get());
        return true;
    }

    @Override
    public Optional<SingleTransaction> findTransactionById(long transactionId) {
        return singleTransactionRepository.findById(transactionId);
    }

    @Override
    public List<SingleTransaction> findAll() {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();
        if (!activeCycle.isPresent()) {
            return Collections.emptyList();
        }
        return singleTransactionRepository.findAllByAppUserAndCycleOrderByIdDesc(currentUser, activeCycle.get());
    }

    @Override
    public boolean addAll(List<SingleTransactionDto> list) throws NoActiveCycleException {
        AppUser appUser = userService.getCurrentLogInUser();
        Cycle currentCycle = cycleService.findActiveCycle().orElseThrow(() -> new NoActiveCycleException(ApplicationActions.ADD_TRANSACTION));
        singleTransactionRepository.saveAll(list.stream()
                .map(dto -> SingleTransactionStaticFactory.createFromDto(dto, appUser, currentCycle))
                .collect(Collectors.toList()));
        LOG.info("Successfully imported %d operations for user with login : ", list.size(), appUser.getLogin());
        return true;
    }

    @Override
    public List<SingleTransaction> findAllUserTransactions() {
        AppUser currentUser = userService.getCurrentLogInUser();
        return singleTransactionRepository.findAllByAppUser(currentUser);
    }
}
