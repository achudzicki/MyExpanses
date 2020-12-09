package com.chudzick.expanses.services.users;

import com.chudzick.expanses.beans.rest.RestRequestBean;
import com.chudzick.expanses.beans.users.UserBean;
import com.chudzick.expanses.domain.settings.dto.UserProfileSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.domain.users.Role;
import com.chudzick.expanses.domain.users.UserDto;
import com.chudzick.expanses.exceptions.LoggedInUserNotFoundException;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.mappers.UserDtoToAppUserMapper;
import com.chudzick.expanses.repositories.RoleRepository;
import com.chudzick.expanses.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder cryptPasswordEncoder;

    @Autowired
    private UserDtoToAppUserMapper userDtoToAppUserMapper;

    @Autowired
    private UserBean userBean;

    @Autowired
    private RestRequestBean restRequestBean;

    private String loginAlreadyExistMessage = "Login already exist";

    @Override
    public Optional<AppUser> findUserByUserName(String userName) {
        return userRepository.findByLogin(userName);
    }

    @Override
    @Transactional
    public void register(UserDto userDto) throws LoginAlreadyExistException {
        LOG.info("Try to register new user {}", userDto.getLogin());

        AppUser userToRegister = userDtoToAppUserMapper.mapObjects(userDto);
        Optional<AppUser> foundUser = findUserByUserName(userToRegister.getLogin());

        if (foundUser.isPresent()) {
            LOG.debug("New user registration failed, {} already exist", userDto.getLogin());
            throw new LoginAlreadyExistException(loginAlreadyExistMessage);
        }

        userToRegister.setPassword(cryptPasswordEncoder.encode(userToRegister.getPassword()));
        setUserRoles(userToRegister);
        userRepository.save(userToRegister);
        LOG.info("New user with userName = {} successful registered", userDto.getLogin());
    }

    @Override
    public AppUser getCurrentLogInUser() {
        if (restRequestBean.isRestRequest()) {
            return restRequestBean.getRequestUser();
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            LOG.error("Can't get current login user, authentication is null");
            throw new LoggedInUserNotFoundException();
        }

        String userName = authentication.getName().trim();
        Optional<AppUser> foundUser = findUserByUserName(userName);

        if (!foundUser.isPresent()) {
            LOG.error("Error while searching for current login AppUser for username: {}", userName);
            throw new UsernameNotFoundException("Can't found user for username: " + userName);
        }

        return foundUser.get();
    }

    @Override
    public AppUser updateUserProfileInformation(UserProfileSettingsDto userDto, AppUser currentUser) {
        currentUser.setName(userDto.getName());
        currentUser.setLastName(userDto.getLastName());
        currentUser.setEmail(userDto.getEmail());
        userBean.setAppUser(currentUser);
        return userRepository.save(currentUser);
    }

    private void setUserRoles(AppUser userToRegister) {
        Set<Role> userRole = new HashSet<>();

        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        if (!role.isPresent()) {
            role = Optional.of(roleRepository.save(new Role("ROLE_USER")));
        }
        userRole.add(role.get());
        userToRegister.setRoles(userRole);
    }

    @Override
    public AppUser findUserById(long id) {
        return userRepository.findById(id).orElseThrow(LoggedInUserNotFoundException::new);
    }

    @Override
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }
}
