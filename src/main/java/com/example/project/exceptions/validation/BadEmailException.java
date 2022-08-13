package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadEmailException extends RuntimeException{
    private final String code = "21";

    public BadEmailException(String message){super(message);}

}
