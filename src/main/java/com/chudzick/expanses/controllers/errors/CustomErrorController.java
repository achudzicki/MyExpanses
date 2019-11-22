package com.chudzick.expanses.controllers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    private static final Logger LOG = LoggerFactory.getLogger(CustomErrorController.class);

    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        LOG.error(String.format("HTTP request method '%s' not supported. URL: %s",request.getMethod(),request.getRequestURI()));
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode.equals(HttpStatus.NOT_FOUND.value())) {
                return "errorPages/404";
            }
        }
        return "errorPages/404";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
