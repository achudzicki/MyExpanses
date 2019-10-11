package com.chudzick.expanses.factories.settings;

import com.chudzick.expanses.domain.settings.UserAvatar;
import com.chudzick.expanses.domain.users.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public final class UserAvatarStaticFactory {
    private static final Logger LOG = LoggerFactory.getLogger(UserAvatarStaticFactory.class);

    private UserAvatarStaticFactory() {
    }

    public static Optional<UserAvatar> fromMultipartFile(MultipartFile multipartFile, AppUser currentUser) {
        try {
            UserAvatar userAvatar = new UserAvatar();
            userAvatar.setAppUser(currentUser);
            userAvatar.setContent(multipartFile.getBytes());
            userAvatar.setName(multipartFile.getName());
            return Optional.of(userAvatar);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }
}
