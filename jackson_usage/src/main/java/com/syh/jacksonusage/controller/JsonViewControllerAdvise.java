package com.syh.jacksonusage.controller;

import com.syh.jacksonusage.exception.BadParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class JsonViewControllerAdvise {

    @ExceptionHandler(BadParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badParameter(BadParameterException exception) {
        return exception.getMessage();
    }
}
