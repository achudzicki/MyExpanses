package com.chudzick.expanses.services;

import com.chudzick.expanses.domain.AppUser;
import com.chudzick.expanses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByLogin(userName);
        user.orElseThrow(() -> new UsernameNotFoundException("AppUser with userName = " + userName + " did not found."));

        return new User(user.get().getLogin(), user.get().getPassword(), Collections.emptySet());
    }
}
