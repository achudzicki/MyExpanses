package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import com.chudzick.expanses.factories.SingleTransactionStaticFactory;
import com.chudzick.expanses.repositories.SingleTransactionRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SingleTransactionServiceImpl implements SingleTransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private SingleTransactionRepository singleTransactionRepository;

    @Override
    @Transactional
    public SingleTransaction addNewTransaction(SingleTransactionDto transactionDto) {
        AppUser appUser = userService.getCurrentLogInUser();

        SingleTransaction newTransaction = SingleTransactionStaticFactory.createFromDto(transactionDto, appUser);
        return singleTransactionRepository.save(newTransaction);
    }

    @Override
    @Transactional
    public List<SingleTransaction> findLastSingleTransactionsLimitBy(int limit) {
        AppUser current = userService.getCurrentLogInUser();

        return singleTransactionRepository.findTop5ByAppUserOrderByIdDesc(current);
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
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<SingleTransaction> singleTransaction = singleTransactionRepository.findById(transactionId);

        if (!singleTransaction.isPresent()) {
            return false;
        }

        if (singleTransaction.get().getAppUser().getId().equals(currentUser.getId())) {
            throw new UserNotPermittedToActionException(ApplicationActions.DELETE_TRANSACTION);
        }

        singleTransactionRepository.delete(singleTransaction.get());
        return true;
    }
}
