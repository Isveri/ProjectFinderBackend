package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class WrongPasswordException extends RuntimeException{
    private final String code = "34";

    public WrongPasswordException(String message){super(message);}

}
