package com.chudzick.expanses.builders;

import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;

import java.util.LinkedList;
import java.util.List;

public class NotificationMessageListBuilder {

    private final List<SimpleNotificationMsg> allNotifications;

    public NotificationMessageListBuilder() {
        this.allNotifications = new LinkedList<>();
    }

    public NotificationMessageListBuilder withSuccessNotification(String msg) {
        SimpleNotificationMsg simpleNotificationMsg = prepareNotification(true, msg);
        allNotifications.add(simpleNotificationMsg);
        return this;
    }

    public NotificationMessageListBuilder withFailureNotificationMsg(String msg) {
        SimpleNotificationMsg simpleNotificationMsg = prepareNotification(false, msg);
        allNotifications.add(simpleNotificationMsg);
        return this;
    }

    public List<SimpleNotificationMsg> getNotificationList() {
        return this.allNotifications;
    }

    private SimpleNotificationMsg prepareNotification(boolean isSuccess, String message) {
        SimpleNotificationMsg simpleNotificationMsg = new SimpleNotificationMsg();
        simpleNotificationMsg.setMessage(message);
        simpleNotificationMsg.setSuccess(isSuccess);
        return simpleNotificationMsg;
    }
}

