package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadCityException extends RuntimeException{
    private final String code = "28";

    public BadCityException(String message){super(message);}

}
