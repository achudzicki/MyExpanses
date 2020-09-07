package com.chudzick.expanses.controllers;

import com.chudzick.expanses.domain.api.ErrorCode;
import com.chudzick.expanses.domain.api.Response;
import com.chudzick.expanses.domain.api.ResponseError;
import com.chudzick.expanses.exceptions.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllersAdvice {
    private static final Logger LOG = LoggerFactory.getLogger(ControllersAdvice.class);

    @ExceptionHandler(LoginAlreadyExistException.class)
    public ResponseEntity<Response> LoginAlreadyExistExceptionHandler(LoginAlreadyExistException ex) {
        LOG.info("Tey to register new user but login already exist");
        Response response = new Response<>
                (Collections.singletonList(new ResponseError(ErrorCode.REGISTER_LOGIN_ALREADY_EXIST)));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotPermittedToActionException.class)
    public ResponseEntity userNotPermittedExceptionHandler(CommonActionExceptions ex) {
        LOG.warn("ERROR " + ex.getAction().getActionName(), ex);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ex.getAction().toString(), ex.getMessage());
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(
            {
                    AppObjectNotFoundException.class,
                    NoActiveCycleException.class
            }
    )
    public ResponseEntity appObjectNotFoundExceptionHandler(CommonActionExceptions ex) {
        LOG.warn("ERROR " + ex.getAction().getActionName(), ex);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ex.getAction().toString(), ex.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImportTransactionException.class)
    public ResponseEntity importTransactionExceptionHandler(CommonActionExceptions ex) {
        LOG.warn("ERROR " + ex.getAction().getActionName(), ex);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ex.getAction().toString(), ex.getMessage());
        return new ResponseEntity<>(jsonObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
