package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{

    private final String code = "1";

    public UserNotFoundException(String message) {
        super(message);
    }
}
