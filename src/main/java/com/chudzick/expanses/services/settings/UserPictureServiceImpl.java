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
    public UserAvatar updatePicture(MultipartFile multipartFile) {
        AppUser currentUser = userService.getCurrentLogInUser();
        deleteOldAvatar(currentUser);
        Optional<UserAvatar> newAvatar = UserAvatarStaticFactory.fromMultipartFile(multipartFile, currentUser);
        if (!newAvatar.isPresent()) {
           return null;
        }
        LOG.info(String.format("Successfully updated picture for user %s", currentUser.getLogin()));
        return userPictureRepository.save(newAvatar.get());
    }

    private void deleteOldAvatar(AppUser currentUser) {
        userPictureRepository.findByAppUser(currentUser)
                .ifPresent(userAvatar -> userPictureRepository.delete(userAvatar));
    }

    @Override
    public Optional<UserAvatar> getPictureByAppUser() {
        AppUser appUser = userService.getCurrentLogInUser();
        return userPictureRepository.findByAppUser(appUser);
    }

    @Override
    public Optional<UserAvatar> getPictureByAppUser(long id) {
        AppUser appUser = userService.findUserById(id);
        return userPictureRepository.findByAppUser(appUser);
    }
}
