package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadUserLimitException extends RuntimeException{
    private final String code = "21";

    public BadUserLimitException(String message){super(message);}

}
