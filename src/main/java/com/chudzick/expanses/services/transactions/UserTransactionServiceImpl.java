package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.*;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.ConstantTransactionStaticFactory;
import com.chudzick.expanses.factories.SingleTransactionStaticFactory;
import com.chudzick.expanses.repositories.ConstantTransactionRepository;
import com.chudzick.expanses.repositories.SingleTransactionRepository;
import com.chudzick.expanses.services.permissions.PermissionsService;
import com.chudzick.expanses.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserTransactionServiceImpl implements UserTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(UserTransactionServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SingleTransactionRepository singleTransactionRepository;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private PermissionsService permissionsService;

    @Autowired
    private ConstantTransactionRepository constantTransactionRepository;

    @Override
    @Transactional
    public SingleTransaction addNewSingleTransaction(SingleTransactionDto transactionDto) throws NoActiveCycleException {
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
    public List<SingleTransaction> findAllSingleTransactionsByGroupId(long groupId) {
        return singleTransactionRepository.findAllByTransactionGroupId(groupId);
    }

    @Override
    @Transactional
    public List<UserTransactions> findAll() {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            return Collections.emptyList();
        }

        List<SingleTransaction> singleTransactions = singleTransactionRepository.findAllByAppUserAndCycleOrderByIdDesc(currentUser, activeCycle.get());
        List<ConstantTransaction> constantTransactions = constantTransactionRepository.findAllByAppUserAndCyclesOrderByIdDesc(currentUser, activeCycle.get());

        return Stream.of(singleTransactions, constantTransactions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteSingleTransactionById(long transactionId) throws UserNotPermittedToActionException {
        Optional<SingleTransaction> singleTransaction = singleTransactionRepository.findById(transactionId);

        if (!singleTransaction.isPresent()) {
            return false;
        }

        permissionsService.checkPermissionToDeleteSingleTransaction(singleTransaction.get());
        singleTransactionRepository.delete(singleTransaction.get());
        return true;
    }

    @Override
    public List<ConstantTransaction> findAllActiveConstantTransactions() {
        AppUser appUser = userService.getCurrentLogInUser();
        return constantTransactionRepository.findAllByAppUserAndActiveOrderByIdDesc(appUser, true);
    }

    @Override
    public ConstantTransaction addNewConstantTransaction(ConstantTransactionDto constantTransactionDto) throws NoActiveCycleException {
        AppUser currentUser = userService.getCurrentLogInUser();
        ConstantTransaction newConstantTransaction;
        if (constantTransactionDto.isAddToActiveCycle()) {
            Optional<Cycle> activeCycle = cycleService.findActiveCycle();
            if (!activeCycle.isPresent()) {
                throw new NoActiveCycleException(ApplicationActions.ADD_CONSTANT_TRANSACTION);
            }
            newConstantTransaction = ConstantTransactionStaticFactory.fromDto(constantTransactionDto, currentUser, activeCycle.get());
            activeCycle.get().getConstantTransactions().add(newConstantTransaction);
        } else {
            newConstantTransaction = ConstantTransactionStaticFactory.fromDto(constantTransactionDto, currentUser);
        }

        return constantTransactionRepository.save(newConstantTransaction);
    }
}
