package com.chudzick.expanses.beans.responses;

import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NotificationMessagesBean {

    private List<SimpleNotificationMsg> notificationsMessages;

    public void setNotificationsMessages(List<SimpleNotificationMsg> notificationsMessages) {
        if (this.notificationsMessages == null || this.notificationsMessages.isEmpty()) {
            this.notificationsMessages = notificationsMessages;
        } else {
            this.notificationsMessages.addAll(notificationsMessages);
        }
    }

    public List<SimpleNotificationMsg> getNotificationsMessages() {
        return notificationsMessages;
    }

}
