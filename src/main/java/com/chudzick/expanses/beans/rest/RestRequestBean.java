package com.chudzick.expanses.beans.rest;

import com.chudzick.expanses.domain.users.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RestRequestBean {
    private AppUser requestUser;
    private boolean restRequest = false;

    public boolean isRestRequest() {
        return restRequest;
    }

    public void setRestRequest(boolean restRequest) {
        this.restRequest = restRequest;
    }

    public AppUser getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(AppUser requestUser) {
        this.requestUser = requestUser;
    }
}
