package com.nju15.cloudnativeproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
// 定义注解
@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class LimitException extends Exception{
    private final String msg;

    public LimitException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
