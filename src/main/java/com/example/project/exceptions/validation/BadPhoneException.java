package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadPhoneException extends RuntimeException{
    private final String code = "27";

    public BadPhoneException(String message){super(message);}

}
