package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class TooLowUserLimitException extends RuntimeException{
    private final String code = "35";

    public TooLowUserLimitException(String message){super(message);}

}
