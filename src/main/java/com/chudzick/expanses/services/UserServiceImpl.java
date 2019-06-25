package com.chudzick.expanses.services;

import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.domain.Role;
import com.chudzick.expanses.domain.UserDto;
import com.chudzick.expanses.exceptions.LoginAlreadyExistException;
import com.chudzick.expanses.mappers.UserDtoToAppUserMapper;
import com.chudzick.expanses.repositories.RoleRepository;
import com.chudzick.expanses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder cryptPasswordEncoder;

    @Autowired
    private UserDtoToAppUserMapper userDtoToAppUserMapper;

    private String loginAlreadyExistMessage = "Login already exist";

    @Override
    public Optional<AppUser> findUserByUserName(String userName) {
        return userRepository.findByLogin(userName);
    }

    @Override
    @Transactional
    public void register(UserDto userDto) throws LoginAlreadyExistException {
        AppUser userToRegister = userDtoToAppUserMapper.mapObjects(userDto);
        Optional<AppUser> foundUser = findUserByUserName(userToRegister.getLogin());

        if (foundUser.isPresent()) {
            throw new LoginAlreadyExistException(loginAlreadyExistMessage);
        }

        userToRegister.setPassword(cryptPasswordEncoder.encode(userToRegister.getPassword()));
        setUserRoles(userToRegister);
        userRepository.save(userToRegister);
    }

    @Override
    public Optional<AppUser> getCurrentLogInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        return findUserByUserName(authentication.getName().trim());
    }

    public void setUserRoles(AppUser userToRegister) {
        Set<Role> userRole = new HashSet<>();

        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        if (!role.isPresent()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        userRole.add(roleRepository.findByName("ROLE_USER").get());
        userToRegister.setRoles(userRole);
    }
}
