package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadUsernameException extends RuntimeException{
    private final String code = "23";

    public BadUsernameException(String message){super(message);}

}
