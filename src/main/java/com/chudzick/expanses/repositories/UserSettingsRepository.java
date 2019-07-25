package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    Optional<UserSettings> findByAppUser(AppUser appUser);
}
