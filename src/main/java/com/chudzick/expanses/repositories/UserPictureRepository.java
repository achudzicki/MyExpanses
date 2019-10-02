package com.chudzick.expanses.repositories;

import com.chudzick.expanses.domain.settings.UserAvatar;
import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPictureRepository extends JpaRepository<UserAvatar,Long> {

    Optional<UserAvatar> findByAppUser(AppUser appUser);
}
