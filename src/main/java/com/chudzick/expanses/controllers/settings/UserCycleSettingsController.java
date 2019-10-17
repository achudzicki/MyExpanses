package com.chudzick.expanses.controllers.settings;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import com.chudzick.expanses.domain.ApplicationActions;
import com.chudzick.expanses.domain.expanses.Cycle;
import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import com.chudzick.expanses.domain.settings.UserSettings;
import com.chudzick.expanses.domain.settings.dto.UserSettingsDto;
import com.chudzick.expanses.exceptions.CycleImpositionException;
import com.chudzick.expanses.exceptions.NoActiveCycleException;
import com.chudzick.expanses.factories.CycleStaticFactory;
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
public class UserCycleSettingsController {
    private final static String NOTIFICATIONS_ATTR_HEADER = "notifications";
    private final static Logger LOG = LoggerFactory.getLogger(UserCycleSettingsController.class);
    private static final String USER_SETTINGS_DTO_ATTR_NAME = "userSettingsDto";

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

        userSettings.ifPresent(userSettings1 -> model.addAttribute("userSettings", userSettings1));
        model.addAttribute("userSettingsDto", new UserSettingsDto());
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "settings/userSettingsMainPage";
    }

    @PostMapping(value = "setup/cycle")
    public String setUpUserCycle(@ModelAttribute @Valid UserSettingsDto userSettingsDto, BindingResult bindingResult, Model model,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request) throws NoActiveCycleException {

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
            LOG.warn(e.getMessage(), e);
            Optional<Cycle> activeCycle = cycleService.findActiveCycle();

            if (!activeCycle.isPresent()) {
                throw new NoActiveCycleException(ApplicationActions.UPDATE_CYCLE_SETTINGS);
            }
            Cycle newCyclePreView = CycleStaticFactory.createNewCyclePreview(userSettingsDto, activeCycle.get());
            request.getSession().setAttribute("userSettingsDto", userSettingsDto);
            model.addAttribute("newCyclePreView", newCyclePreView);
            model.addAttribute("activeCycle", activeCycle.get());
            model.addAttribute(USER_SETTINGS_DTO_ATTR_NAME, userSettingsDto);
            return "transaction/cycleImpositionAlert";
        }

        redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_HEADER, new NotificationMessageListBuilder()
                .withSuccessNotification("Dane poprawnie ustawione")
                .getNotificationList());
        return "redirect:/settings";
    }

    @PostMapping(value = "setup/cycle/approve/change")
    public String approveCycleChange(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        UserSettingsDto userSettingsDto = (UserSettingsDto) request.getSession().getAttribute(USER_SETTINGS_DTO_ATTR_NAME);

        if (userSettingsDto == null) {
            LOG.error("Force save user settings - Session attribute expected but not found!");
            redirectAttributes.addFlashAttribute(NOTIFICATIONS_ATTR_HEADER, new NotificationMessageListBuilder()
                    .withFailureNotificationMsg("Błąd podczas ustawiania danych")
                    .getNotificationList());
            return "redirect:/settings";
        }

        userSettingsService.forceSave(userSettingsDto);
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
