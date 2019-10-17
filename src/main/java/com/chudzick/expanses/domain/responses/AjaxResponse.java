package com.chudzick.expanses.domain.responses;

import org.json.JSONObject;

public final class AjaxResponse {
    private final AjaxResponseStatus ajaxResponseStatus;
    private final String notificationMessage;
    private String contentHTML;

    private AjaxResponse(AjaxResponseBuilder ajaxResponseBuilder) {
        this.ajaxResponseStatus = ajaxResponseBuilder.ajaxResponseStatus;
        this.notificationMessage = ajaxResponseBuilder.notificationMessage;
        this.contentHTML = ajaxResponseBuilder.contentHTML;
    }

    public AjaxResponseStatus getAjaxResponseStatus() {
        return ajaxResponseStatus;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getContentHTML() {
        return contentHTML;
    }

    public static final class AjaxResponseBuilder {
        private AjaxResponseStatus ajaxResponseStatus;
        private String notificationMessage;
        private String contentHTML;

        public AjaxResponseBuilder(AjaxResponseStatus ajaxResponseStatus, String notificationMessage) {
            this.ajaxResponseStatus = ajaxResponseStatus;
            this.notificationMessage = notificationMessage;
        }

        public AjaxResponseBuilder withContent(String content) {
            this.contentHTML = content;
            return this;
        }

        public AjaxResponse build() {
            return new AjaxResponse(this);
        }

        public String buildAsJson() {
            return new JSONObject(build()).toString();
        }
    }
}
