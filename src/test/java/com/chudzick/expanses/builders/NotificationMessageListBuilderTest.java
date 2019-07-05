package com.chudzick.expanses.builders;

import com.chudzick.expanses.domain.responses.SimpleNotificationMsg;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class NotificationMessageListBuilderTest {

    @Test
    public void prepareNotificationListTest() {
        List<SimpleNotificationMsg> allNotifcations = new NotificationMessageListBuilder()
                .withSuccessNotification("success")
                .withFailureNotificationMsg("fail")
                .getNotificationList();

        Assert.assertEquals(allNotifcations.size(), 2);
        Assert.assertEquals(allNotifcations.get(0).getMessage(), "success");
        Assert.assertFalse(allNotifcations.get(1).isSuccess());
    }
}
