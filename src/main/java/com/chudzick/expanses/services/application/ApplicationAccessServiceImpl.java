package com.chudzick.expanses.services.application;

import com.chudzick.expanses.domain.application.ApplicationAccess;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.repositories.ApplicationAccessRepository;
import com.chudzick.expanses.services.users.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationAccessServiceImpl implements ApplicationAccessService {
    private static final int APPLICATION_ID_LENGTH = 30;


    @Autowired
    private ApplicationAccessRepository applicationAccessRepository;

    @Autowired
    private UserService userService;

    @Override
    public ApplicationAccess save(ApplicationAccess applicationAccess) {
        return applicationAccessRepository.save(applicationAccess);
    }

    @Override
    public Optional<ApplicationAccess> findByApplicationId(String applicationId) {
        return applicationAccessRepository.findByApplicationId(applicationId);
    }

    @Override
    public List<ApplicationAccess> findByCurrentUser() {
        AppUser currentUser = userService.getCurrentLogInUser();
        return applicationAccessRepository.findAllByAppUser(currentUser);
    }

    @Override
    public ApplicationAccess prepareNew(String description) {
        AppUser currentUser = userService.getCurrentLogInUser();
        ApplicationAccess applicationAccess = new ApplicationAccess();
        applicationAccess.setDescription(description);
        applicationAccess.setAppUser(currentUser);
        applicationAccess.setApplicationId(RandomStringUtils.random(APPLICATION_ID_LENGTH, true, true));
        return applicationAccess;
    }
}
