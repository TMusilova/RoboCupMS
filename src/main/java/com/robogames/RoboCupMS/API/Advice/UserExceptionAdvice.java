package com.robogames.RoboCupMS.API.Advice;

import com.robogames.RoboCupMS.API.Exception.UserException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UserException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserException.NotFound _ex) {
        return _ex.getMessage();
    }

}
