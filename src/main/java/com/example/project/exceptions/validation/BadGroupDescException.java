package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadGroupDescException extends RuntimeException{
    private final String code = "30BadEmailException";

    public BadGroupDescException(String message){super(message);}

}
