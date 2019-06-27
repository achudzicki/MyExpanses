package com.chudzick.expanses.repositories.users;

import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.AppUserStaticFactory;
import com.chudzick.expanses.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest implements TestUserSupplier {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userRepositoryNotNull() {
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void userRepositoryShouldReturnOptionalEmptyWhenNoUserFound() {
        Assert.assertEquals(userRepository.findByLogin("BAD_LOGIN"), Optional.empty());
    }

    @Test
    public void userRepositoryShouldSaveUser() {
        AppUser appUser = AppUserStaticFactory.createFromDto(prepareValidUserDto());

        Assert.assertEquals(userRepository.findByLogin(appUser.getLogin()), Optional.empty());

        userRepository.save(appUser);

        Assert.assertEquals(userRepository.findByLogin(appUser.getLogin()).get().getName(), appUser.getName());
        Assert.assertEquals(userRepository.findByLogin(appUser.getLogin()).get().getLastName(), appUser.getLastName());
        Assert.assertEquals(userRepository.findByLogin(appUser.getLogin()).get().getPassword(), appUser.getPassword());
    }
}
