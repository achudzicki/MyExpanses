package com.chudzick.expanses.controllers;

import com.chudzick.expanses.exceptions.UserNotPermittedToActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllersAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ControllersAdvice.class);

    @ExceptionHandler(UserNotPermittedToActionException.class)
    public ModelAndView userNotPermittedExceptionHandler(UserNotPermittedToActionException ex) {
        LOG.warn("Access violation to " + ex.getAction().getActionName(), ex);
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("action",ex.getAction());
        modelAndView.addObject("message",ex.getMessage());

        modelAndView.setViewName("alerts/permissionDeny");
        return modelAndView;
    }
}
