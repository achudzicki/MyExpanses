package com.chudzick.expanses.controllers.settings;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.settings.dto.UserProfileSettingsDto;
import com.chudzick.expanses.domain.users.AppUser;
import com.chudzick.expanses.services.settings.UserPictureService;
import com.chudzick.expanses.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Encoder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "settings")
public class UserProfileSettingsController {
    private static final Logger LOG = LoggerFactory.getLogger(UserProfileSettingsController.class);
    private final static String NOTIFICATIONS_ATTR_HEADER = "notifications";

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @Autowired
    private UserPictureService userPictureService;

    @GetMapping(value = "profile")
    public String initProfilePage(@ModelAttribute(NOTIFICATIONS_ATTR_HEADER) List<SimpleNotificationMsg> notificationMsg, Model model) {
        AppUser currentUser = userService.getCurrentLogInUser();
        model.addAllAttributes(profileSettingModelMap(currentUser, notificationMsg));
        return "settings/profileSettings";
    }

    @PostMapping(value = "profile")
    public String updateProfileSettings(@Valid @ModelAttribute("userSettingsDto") UserProfileSettingsDto userDto,
                                        BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        AppUser currentUser = userService.getCurrentLogInUser();

        if (bindingResult.hasErrors()) {
            LOG.warn(String.format("User %s tired change profile settings, binding errors appeared", currentUser.getLogin()));
            model.addAllAttributes(profileSettingModelMap(currentUser, new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Błąd podczas zmiany ustawień profilu")
                    .getNotificationList()));
            model.addAttribute("bindingResult", bindingResult);
            return "settings/profileSettings";
        } else {
            userService.updateUserProfileInformation(userDto, currentUser);
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_HEADER, new NotificationMessageListBuilder()
                    .withSuccessNotification("Poprawnie zmieniono ustawienia profilu")
                    .getNotificationList());
        }
        return "redirect:/settings/profile";
    }

    @PostMapping(value = "/profile/picture")
    @ResponseBody
    public String saveProfilePicture(@RequestParam("file") MultipartFile file) {
        LOG.info("Update user picture controller invoked");
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(userPictureService.updatePicture(file).getContent());
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_HEADER)
    private List<SimpleNotificationMsg> defaultNotificationList() {
        return new ArrayList<>();
    }

    private Map<String, Object> profileSettingModelMap(AppUser currentUser, List<SimpleNotificationMsg> notificationMsg) {
        Map<String, Object> profileSettingModelMap = new HashMap<>();
        notificationMessagesBean.setNotificationsMessages(notificationMsg);
        profileSettingModelMap.put("user", currentUser);
        profileSettingModelMap.put("userSettingsDto", UserProfileSettingsDto.fromAppUser(currentUser));
        profileSettingModelMap.put("notificationMessagesBean", notificationMessagesBean);
        return profileSettingModelMap;
    }
}
