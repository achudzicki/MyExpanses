package com.chudzick.expanses.controllers.picture;

import com.chudzick.expanses.domain.settings.UserAvatar;
import com.chudzick.expanses.services.settings.UserPictureService;
import com.chudzick.expanses.util.picture.ReturnAvatarHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class UserPictureController {
    private static final Logger LOG = LoggerFactory.getLogger(UserPictureController.class);

    @Autowired
    private UserPictureService userPictureService;

    @GetMapping(value = "picture/profile")
    public void getUserPicture(HttpServletResponse response) {
        Optional<UserAvatar> userAvatar = userPictureService.getPictureByAppUser();
        ReturnAvatarHelper.returnAvatar(userAvatar, response);
    }

    @GetMapping(value = "picture/profile/{id}")
    public void getUserPictureById(@PathVariable long id, HttpServletResponse response) {
        Optional<UserAvatar> userAvatar = userPictureService.getPictureByAppUser(id);
        ReturnAvatarHelper.returnAvatar(userAvatar, response);
    }
}
