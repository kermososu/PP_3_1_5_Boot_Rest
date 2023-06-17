package ru.kata.spring.boot_security.demo.util;

import java.util.Date;

public class UserErrorResponse {

    private String message;

    private Date time;

    public UserErrorResponse(String message, Date time) {
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
