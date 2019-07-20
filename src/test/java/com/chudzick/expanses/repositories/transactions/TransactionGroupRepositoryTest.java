package com.chudzick.expanses.repositories.transactions;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.expanses.TransactionGroup;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.repositories.TransactionGroupRepository;
import com.chudzick.expanses.services.users.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionGroupRepositoryTest implements TestUserSupplier {

    private final String TEST_LOGIN = "testLogin";

    @Autowired
    private TransactionGroupRepository groupRepository;

    @Autowired
    private UserService userService;


    @Test
    public void repositoryNotNull() {
        Assert.assertNotNull(groupRepository);
    }

    @Test
    public void saveTransactionGroupTest() throws LoginAlreadyExistException {
        TransactionGroup transactionGroup1 = addGroup(TEST_LOGIN);

        Assert.assertNotNull(transactionGroup1);
        Assert.assertNotNull(transactionGroup1.getId() != null);
        Assert.assertNotNull(transactionGroup1.getId() != 0);
    }

    @Test
    public void getAllTest() throws LoginAlreadyExistException {

        for (int i = 0; i < 5; i++) {
            addGroup(TEST_LOGIN + i);
        }

        List<TransactionGroup> result = groupRepository.findAll();

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 5);
    }

    private TransactionGroup addGroup(String login) throws LoginAlreadyExistException {
        TransactionGroup transactionGroup = new TransactionGroup();
        UserDto userDto = prepareValidUserDto();
        userDto.setLogin(login);


        userService.register(userDto);
        AppUser appUser = userService.findUserByUserName(userDto.getLogin()).get();

        transactionGroup.setGorupName("testName");
        transactionGroup.setGroupDescription("testDescription");
        transactionGroup.setAppUser(appUser);

        return groupRepository.save(transactionGroup);
    }
}
