package com.chudzick.expanses.domain.responses;

public final class AjaxResponse {
    private final AjaxResponseStatus ajaxResponseStatus;
    private final String notificationMessage;

    public AjaxResponse(AjaxResponseStatus ajaxResponseStatus, String notificationMessage) {
        this.ajaxResponseStatus = ajaxResponseStatus;
        this.notificationMessage = notificationMessage;
    }

    public static AjaxResponse successResponseFrom(String notificationMessage) {
        return new AjaxResponse(AjaxResponseStatus.SUCCESS, notificationMessage);
    }

    public static AjaxResponse failureResponseFrom(String notificationMessage) {
        return new AjaxResponse(AjaxResponseStatus.ERROR, notificationMessage);
    }

    public AjaxResponseStatus getAjaxResponseStatus() {
        return ajaxResponseStatus;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
}
