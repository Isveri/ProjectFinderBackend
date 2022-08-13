package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadAgeException extends RuntimeException{
    private final String code = "26";

    public BadAgeException(String message){super(message);}

}
