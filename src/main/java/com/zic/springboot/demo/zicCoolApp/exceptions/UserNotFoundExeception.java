package com.zic.springboot.demo.zicCoolApp.exceptions;

public class UserNotFoundExeception extends RuntimeException{
    public UserNotFoundExeception() {
    }

    public UserNotFoundExeception(String message) {
        super(message);
    }

    public UserNotFoundExeception(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundExeception(Throwable cause) {
        super(cause);
    }


}
