package com.chudzick.expanses.controllers;

import com.chudzick.expanses.beans.responses.NotificationMessagesBean;
import com.chudzick.expanses.builders.NotificationMessageListBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MultipleSessionsController {

    @Autowired
    private NotificationMessagesBean notificationMessagesBean;

    @GetMapping(value = "multiple/sessions")
    public String getMultipleSessionsView(Model model) {
        notificationMessagesBean.setNotificationsMessages(new NotificationMessageListBuilder()
                .withFailureNotificationMsg("Użytkownik zalogował się do systemu z innej przeglądarki")
                .getNotificationList());
        model.addAttribute("notificationMessagesBean", notificationMessagesBean);
        return "login";
    }
}
