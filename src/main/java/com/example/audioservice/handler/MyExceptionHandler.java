package com.example.audioservice.handler;

import com.example.audioservice.model.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Ruby
 * @create 2020-10-02 09:54:12
 */
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public R handleException(Exception e) {
        return new R(9999, "error",null, e.getMessage());
    }
}
