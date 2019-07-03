package com.chudzick.expanses.domain.responses;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NotificationMessagesBean {

    private List<SimpleNotificationMsg> notificationsMessages;

    public void setNotificationsMessages(List<SimpleNotificationMsg> notificationsMessages) {
        this.notificationsMessages = notificationsMessages;
    }

    public List<SimpleNotificationMsg> getNotificationsMessages() {
        return notificationsMessages;
    }

}
