package com.chudzick.expanses.services;


import com.chudzick.expanses.TestUserSupplier;
import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.Role;
import com.chudzick.expanses.domain.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.mappers.UserDtoToAppUserMapper;
import com.chudzick.expanses.repositories.RoleRepository;
import com.chudzick.expanses.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest implements TestUserSupplier {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoToAppUserMapper appUserMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnOptionalEmptyIfUserNotFind() {
        String wrongLogin = "WRONG_LOGIN";

        when(userRepository.findByLogin(wrongLogin)).thenReturn(Optional.empty());

        Assert.assertEquals(userService.findUserByUserName(wrongLogin), Optional.empty());
    }

    @Test
    public void shouldReturnOptionalOfUserIfFound() {
        String foundLogin = "FOUND_LOGIN";
        AppUser appUser = new AppUser();
        appUser.setLogin(foundLogin);
        when(userRepository.findByLogin(foundLogin)).thenReturn(Optional.of(appUser));

        Assert.assertEquals(userService.findUserByUserName(foundLogin).get().getLogin(), foundLogin);
    }

    @Test(expected = LoginAlreadyExistException.class)
    public void registerShouldThrowLoginAlreadyExistError() throws LoginAlreadyExistException {
        UserDto userDto = prepareValidUserDto();
        AppUser appUser = new AppUser();
        appUser.setLogin("LOGIN_LOGIN");

        when(appUserMapper.mapObjects(userDto)).thenReturn(appUser);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(appUser));

        userService.register(userDto);
    }

    @Test
    public void registerShouldRegisterNewUserIfLoginNotExist() throws LoginAlreadyExistException {
        UserDto userDto = prepareValidUserDto();
        AppUser appUser = new AppUser();
        appUser.setLogin("LOGIN_LOGIN");
        appUser.setPassword("PASSWORD_RAW");

        when(appUserMapper.mapObjects(userDto)).thenReturn(appUser);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(appUser)).thenReturn(appUser);
        when(userRepository.save(appUser)).thenReturn(appUser);
        when(passwordEncoder.encode(anyString())).thenReturn("HASHPASSWORD");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_USER")));
        userService.register(userDto);

        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    public void getCurrentLoginUserTest() {
        AppUser appUser = new AppUser();
        String currentLogin = "Current_login";
        appUser.setLogin(currentLogin);


        when(mockSecurityContext().getName()).thenReturn(currentLogin);
        when(userRepository.findByLogin(currentLogin)).thenReturn(Optional.of(appUser));

        Optional<AppUser> logInUser = userService.getCurrentLogInUser();

        Assert.assertTrue(logInUser.isPresent());
        Assert.assertEquals(logInUser.get().getLogin(), currentLogin);
    }


    private Authentication mockSecurityContext() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        return authentication;
    }
}
