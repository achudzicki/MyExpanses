package com.chudzick.expanses.services.users;

import com.chudzick.expanses.domain.auth.AuthUserDetails;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByLogin(userName);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("AppUser with userName = " + userName + " did not found.");
        }

        return new AuthUserDetails(user.get());
    }
}
