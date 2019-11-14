package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.ConstantTransaction;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.expanses.dto.ConstTransactionDeleteDto;
import com.chudzick.expanses.domain.expanses.dto.ConstantTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.AppObjectNotFoundException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.ConstantTransactionStaticFactory;
import com.chudzick.expanses.repositories.ConstantTransactionRepository;
import com.chudzick.expanses.services.permissions.PermissionsService;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConstantTransactionServiceImpl implements ConstantTransactionService<ConstantTransaction, ConstantTransactionDto> {

    @Autowired
    private ConstantTransactionRepository constantTransactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CycleService cycleService;

    @Autowired
    private PermissionsService permissionsService;

    @Override
    public ConstantTransaction addNewTransaction(ConstantTransactionDto dto) throws NoActiveCycleException {
        AppUser currentUser = userService.getCurrentLogInUser();
        ConstantTransaction newConstantTransaction;
        if (dto.isAddToActiveCycle()) {
            Optional<Cycle> activeCycle = cycleService.findActiveCycle();
            if (!activeCycle.isPresent()) {
                throw new NoActiveCycleException(ApplicationActions.ADD_CONSTANT_TRANSACTION);
            }
            newConstantTransaction = ConstantTransactionStaticFactory.fromDto(dto, currentUser, activeCycle.get());
            activeCycle.get().getConstantTransactions().add(newConstantTransaction);
        } else {
            newConstantTransaction = ConstantTransactionStaticFactory.fromDto(dto, currentUser);
        }

        return constantTransactionRepository.save(newConstantTransaction);
    }

    @Override
    public int countTransactionsByGroup(long groupId) {
        return constantTransactionRepository.countAllByTransactionGroupId(groupId);
    }

    @Override
    public List<ConstantTransaction> findAllTransactionsByGroupId(long groupId) {
        return constantTransactionRepository.findAllByTransactionGroupId(groupId);
    }

    @Override
    public boolean deleteTransactionById(long transactionId) throws UserNotPermittedToActionException, AppObjectNotFoundException {
        ConstantTransaction constantTransaction = findTransactionById(transactionId).orElseThrow(() ->
                new AppObjectNotFoundException(ApplicationActions.DELETE_CONSTANT_TRANSACTION, "Nie znaleziono transakcji do usunięcia"));
        permissionsService.checkPermissionToDeleteTransaction(constantTransaction);
        constantTransactionRepository.delete(constantTransaction);
        return true;
    }

    @Override
    public Optional<ConstantTransaction> findTransactionById(long transactionId) {
        return constantTransactionRepository.findById(transactionId);
    }

    @Override
    public List<ConstantTransaction> findAll() {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<Cycle> activeCycle = cycleService.findActiveCycle();

        if (!activeCycle.isPresent()) {
            return Collections.emptyList();
        }

        return constantTransactionRepository.findAllByAppUserAndCyclesOrderByIdDesc(currentUser, activeCycle.get());
    }

    @Override
    public boolean addAll(List<ConstantTransactionDto> list) {
        AppUser appUser = userService.getCurrentLogInUser();
        constantTransactionRepository.saveAll(list.stream()
                .map(constantTransactionDto -> ConstantTransactionStaticFactory.fromDto(constantTransactionDto,appUser))
                .collect(Collectors.toList()));
        return true;
    }

    @Override
    public List<ConstantTransaction> findAllUserTransactions() {
        AppUser currentUser = userService.getCurrentLogInUser();
        return constantTransactionRepository.findAllByAppUser(currentUser);
    }

    @Override
    public List<ConstantTransaction> findAllActiveConstantTransactions() {
        AppUser appUser = userService.getCurrentLogInUser();
        return constantTransactionRepository.findAllByAppUserAndActiveOrderByIdDesc(appUser, true);
    }

    @Override
    public void deleteTransactionsFromCycles(long transactionId, List<ConstTransactionDeleteDto> constantTransDto) throws AppObjectNotFoundException, UserNotPermittedToActionException {
        ConstantTransaction constantTransaction = findTransactionById(transactionId).orElseThrow(() ->
                new AppObjectNotFoundException(ApplicationActions.DELETE_CONSTANT_TRANSACTION, "Nie znaleziono transakcji do usunięcia"));
        permissionsService.checkPermissionToDeleteTransaction(constantTransaction);

        Set<Long> cyclesToRemoveTransaction = constantTransDto.stream()
                .filter(ConstTransactionDeleteDto::isDelete)
                .map(ConstTransactionDeleteDto::getCycleId).collect(Collectors.toSet());

        constantTransaction.setActive(false);
        constantTransaction.getCycles().removeIf(
                cycle -> cyclesToRemoveTransaction.contains(cycle.getId())
        );
        constantTransactionRepository.save(constantTransaction);
    }

    @Override
    public List<ConstantTransaction> renewConstantTransactions(Cycle newCycle, AppUser appUser) {
        List<ConstantTransaction> renewalTransactions = findAllActiveByAppUser(appUser);
        renewalTransactions.forEach(transaction -> {
            if (transaction.isPermanentDuration()) {
                transaction.getCycles().add(newCycle);
            } else {
                if (transaction.getCyclesAppears() < transaction.getCyclesCount()) {
                    transaction.setCyclesAppears(transaction.getCyclesAppears() + 1);
                    transaction.getCycles().add(newCycle);
                } else {
                    transaction.setActive(false);
                }
            }
        });
        constantTransactionRepository.saveAll(renewalTransactions);
        return renewalTransactions;
    }

    private List<ConstantTransaction> findAllActiveByAppUser(AppUser appUser) {
        return constantTransactionRepository.findAllByAppUserAndActiveOrderByIdDesc(appUser,true);
    }
}
