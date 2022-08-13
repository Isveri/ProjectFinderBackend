package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadProfileInfoException extends RuntimeException{
    private final String code = "29";

    public BadProfileInfoException(String message){super(message);}

}
