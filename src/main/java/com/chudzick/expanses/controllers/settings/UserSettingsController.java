package com.chudzick.expanses.controllers.settings;

import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.responses.NotificationMessagesBean;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.exceptions.CycleImpositionException;
import com.chudzick.expanses.services.settings.UserSettingsService;
import com.chudzick.expanses.services.transactions.CycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/settings")
public class UserSettingsController {
    private final static String NOTIFICATIONS_ATTR_HEADER = "notifications";
    private final static Logger LOG = LoggerFactory.getLogger(UserSettingsController.class);

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @Autowired
    private CycleService cycleService;


    @GetMapping
    public String initUserSettingsPage(@ModelAttribute(NOTIFICATIONS_ATTR_HEADER) List<SimpleNotificationMsg> notifications, Model model) {
        Optional<UserSettings> userSettings = userSettingsService.findUserSettings();


        notificationMessagesBean.setNotificationsMessages(notifications);

        if (userSettings.isPresent()) {
            model.addAttribute("userSettings", userSettings.get());
        }
        model.addAttribute("userSettingsDto", new UserSettingsDto());
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "settings/userSettingsMainPage";
    }

    @PostMapping(value = "setup/cycle")
    public String setUpUserCycle(@ModelAttribute @Valid UserSettingsDto userSettingsDto, BindingResult bindingResult, Model model,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            Optional<UserSettings> userSettings = userSettingsService.findUserSettings();
            notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Błąd podczas zapisywania ustawień")
                    .getNotificationList());

            model.addAttribute("notificationMessagesBean", notificationMessagesBean);
            userSettings.ifPresent(settings -> model.addAttribute("userSettings", settings));
            return "settings/userSettingsMainPage";
        }

        try {
            userSettingsService.saveOrUpdate(userSettingsDto);
        } catch (CycleImpositionException e) {
            LOG.warn(e.getMessage());
            e.printStackTrace();
            Optional<Cycle> activeCycle = cycleService.findActiveCycle();

            model.addAttribute("activeCycle", activeCycle.get());
            model.addAttribute("userSettingsDto", userSettingsDto);
            return "transaction/cycleImpositionAlert";
        }

        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_HEADER, new NotificationMessageListBuilder()
                .withSuccessNotification("Dane poprawnie ustawione")
                .getNotificationList());
        return "redirect:/settings";
    }

    @ModelAttribute(NOTIFICATIONS_ATTR_HEADER)
    private List<SimpleNotificationMsg> defaultNotificationList() {
        return new ArrayList<>();
    }

}
