package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class UsernameAlreadyTakenException extends RuntimeException{
    private final String code = "32";

    public UsernameAlreadyTakenException(String message){super(message);}

}
