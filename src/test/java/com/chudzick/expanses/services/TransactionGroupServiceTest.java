package com.chudzick.expanses.services;

import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.expanses.TransactionGroupDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.TransactionGroupStaticFactory;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.transactions.TransactionGroupServiceImpl;
import com.chudzick.expanses.services.users.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionGroupServiceTest {

    private AppUser appUser = new AppUser();
    @InjectMocks
    private TransactionGroupServiceImpl transactionGroupService;

    @Mock
    private UserService userService;

    @Mock
    private TransactionGroupRepository transactionGroupRepository;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        when(userService.getCurrentLogInUser()).thenReturn(appUser);
    }

    @Test
    public void addTransactionGroupTest() {
        TransactionGroupDto transactionGroupDto = new TransactionGroupDto();
        transactionGroupDto.setGorupName("testName");
        transactionGroupDto.setGroupDescription("testDesc");

        TransactionGroup transactionGroup = TransactionGroupStaticFactory.createFromDto(transactionGroupDto, new AppUser());

        when(userService.getCurrentLogInUser()).thenReturn(new AppUser());
        when(transactionGroupRepository.save(any(TransactionGroup.class))).thenReturn(transactionGroup);

        transactionGroupService.addNewTransactionGroup(transactionGroupDto);

        verify(userService, times(1)).getCurrentLogInUser();
        verify(transactionGroupRepository, times(1)).save(any(TransactionGroup.class));
    }

    @Test
    public void getAllTransactionsGroupTest() {
        List<TransactionGroup> transactionGroupList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            transactionGroupList.add(new TransactionGroup());
        }

        when(transactionGroupRepository.findAllByAppUser(appUser)).thenReturn(transactionGroupList);

        List<TransactionGroup> result = transactionGroupService.getAllGroups();

        Assert.assertEquals(result.size(), transactionGroupList.size());
    }


}
