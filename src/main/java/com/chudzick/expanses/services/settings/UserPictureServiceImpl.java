package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.settings.UserAvatar;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.factories.settings.UserAvatarStaticFactory;
import com.chudzick.expanses.repositories.UserPictureRepository;
import com.chudzick.expanses.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserPictureServiceImpl implements UserPictureService {
    private static final Logger LOG = LoggerFactory.getLogger(UserPictureServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserPictureRepository userPictureRepository;

    @Override
    public boolean updatePicture(MultipartFile multipartFile) {
        AppUser currentUser = userService.getCurrentLogInUser();
        Optional<UserAvatar> userAvatar = UserAvatarStaticFactory.fromMultipartFile(multipartFile, currentUser);
        if (!userAvatar.isPresent()) {
            return false;
        }
        userPictureRepository.save(userAvatar.get());
        LOG.info(String.format("Successfully updated picture for user %s", currentUser.getLogin()));
        return true;
    }

    @Override
    public Optional<UserAvatar> getPictureByAppUser() {
        AppUser appUser = userService.getCurrentLogInUser();
        return userPictureRepository.findByAppUser(appUser);
    }
}
