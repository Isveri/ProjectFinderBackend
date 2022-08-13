package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class BadGroupNameException extends RuntimeException{
    private final String code = "25";

    public BadGroupNameException(String message){super(message);}

}
