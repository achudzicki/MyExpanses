package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.application.ApplicationAccess;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationAccessRepository extends JpaRepository<ApplicationAccess, Long> {

    List<ApplicationAccess> findAllByAppUser(AppUser appUser);

    Optional<ApplicationAccess> findByApplicationId(String applicationId);
}
