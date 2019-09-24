package com.chudzick.expanses.domain.responses;

import java.util.List;
import java.util.Optional;

public final class AjaxResponse<T> {
    private final AjaxResponseStatus ajaxResponseStatus;
    private final String notificationMessage;
    private Optional<List<T>> content;

    private AjaxResponse(AjaxResponseBuilder ajaxResponseBuilder) {
        this.ajaxResponseStatus = ajaxResponseBuilder.ajaxResponseStatus;
        this.notificationMessage = ajaxResponseBuilder.notificationMessage;
        this.content = Optional.ofNullable(ajaxResponseBuilder.content);
    }

    public AjaxResponseStatus getAjaxResponseStatus() {
        return ajaxResponseStatus;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public Optional<List<T>> getContent() {
        return content;
    }

    public static final class AjaxResponseBuilder<T> {
        private AjaxResponseStatus ajaxResponseStatus;
        private String notificationMessage;
        private List<T> content;

        public AjaxResponseBuilder(AjaxResponseStatus ajaxResponseStatus, String notificationMessage) {
            this.ajaxResponseStatus = ajaxResponseStatus;
            this.notificationMessage = notificationMessage;
        }

        public AjaxResponseBuilder withContent(List<T> content) {
            this.content = content;
            return this;
        }

        public AjaxResponse build() {
            AjaxResponse ajaxResponse = new AjaxResponse(this);
            return ajaxResponse;
        }
    }
}
