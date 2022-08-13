package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadPasswordException extends RuntimeException{
    private final String code = "22";

    public BadPasswordException(String message){super(message);}

}
