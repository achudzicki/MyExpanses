package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SingleTransactionServiceImpl implements SingleTransactionService {

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
    @Transactional
    public SingleTransaction addNewTransaction(SingleTransactionDto transactionDto) throws NoActiveCycleException {
        AppUser appUser = userService.getCurrentLogInUser();
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            LOG.warn("Try to add new transaction with no active cycle");
            throw new NoActiveCycleException(ApplicationActions.ADD_TRANSACTION);
        }

        SingleTransaction newTransaction = SingleTransactionStaticFactory.createFromDto(transactionDto, appUser, activeCycle.get());
        return singleTransactionRepository.save(newTransaction);
    }

    @Override
    @Transactional
    public List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit) {
        AppUser current = userService.getCurrentLogInUser();
        Optional<Cycle> currentCycle = cycleService.findActiveCycle();

        if (!currentCycle.isPresent()) {
            return Collections.emptyList();
        }

        return singleTransactionRepository.findTop5ByAppUserAndCycleOrderByIdDesc(current, currentCycle.get());
    }

    @Override
    @Transactional
    public int countTransactionsByGroup(long groupId) {
        return singleTransactionRepository.countSingleTransactionByTransactionGroupId(groupId);
    }

    @Override
    @Transactional
    public List<SingleTransaction> findAllByGroupId(long groupId) {
        return singleTransactionRepository.findAllByTransactionGroupId(groupId);
    }

    @Override
    @Transactional
    public List<SingleTransaction> findAll() {
        AppUser currentUser = userService.getCurrentLogInUser();

        return singleTransactionRepository.findAllByAppUser(currentUser);
    }

    @Override
    public boolean deleteTransactionById(long transactionId) throws UserNotPermittedToActionException {
        Optional<SingleTransaction> singleTransaction = singleTransactionRepository.findById(transactionId);

        if (!singleTransaction.isPresent()) {
            return false;
        }

        permissionsService.checkPermissionToDeleteSingleTransaction(singleTransaction.get());
        singleTransactionRepository.delete(singleTransaction.get());
        return true;
    }
}
