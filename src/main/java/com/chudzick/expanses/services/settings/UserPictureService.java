package com.chudzick.expanses.services.settings;

import com.chudzick.expanses.domain.settings.UserAvatar;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserPictureService {

    boolean updatePicture(MultipartFile multipartFile);

    Optional<UserAvatar> getPictureByAppUser();
}
