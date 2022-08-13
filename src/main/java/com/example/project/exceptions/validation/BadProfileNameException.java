package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadProfileNameException extends RuntimeException{
    private final String code = "24";

    public BadProfileNameException(String message){super(message);}

}
