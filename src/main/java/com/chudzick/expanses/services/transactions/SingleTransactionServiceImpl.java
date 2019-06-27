package com.chudzick.expanses.services.transactions;

import com.chudzick.expanses.domain.expanses.SingleTransaction;
import com.chudzick.expanses.domain.expanses.SingleTransactionDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.exceptions.LoggedInUserNotFoundException;
import com.chudzick.expanses.factories.SingleTransactionStaticFactory;
import com.chudzick.expanses.repositories.transactionsApp.SingleTransactionRepository;
import com.chudzick.expanses.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SingleTransactionServiceImpl implements SingleTransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private SingleTransactionRepository singleTransactionRepository;

    @Override
    public SingleTransaction addNewTransaction(SingleTransactionDto transactionDto) {
        Optional<AppUser> appUser = userService.getCurrentLogInUser();

        if (!appUser.isPresent()) {
            throw new LoggedInUserNotFoundException();
        }

        SingleTransaction newTransaction = SingleTransactionStaticFactory.createFromDto(transactionDto, appUser.get());


        return singleTransactionRepository.save(newTransaction);
    }
}
