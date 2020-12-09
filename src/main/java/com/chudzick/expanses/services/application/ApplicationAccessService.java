package com.chudzick.expanses.services.application;

import com.chudzick.expanses.domain.application.ApplicationAccess;

import java.util.List;
import java.util.Optional;

public interface ApplicationAccessService {
    Optional<ApplicationAccess> findByApplicationId(String applicationId);

    List<ApplicationAccess> findByCurrentUser();

    ApplicationAccess prepareNew(String description);

    ApplicationAccess save(ApplicationAccess applicationAccess);
}
