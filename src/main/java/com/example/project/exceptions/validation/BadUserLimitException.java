package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadUserLimitException extends RuntimeException{
    private final String code = "36";

    public BadUserLimitException(String message){super(message);}

}
