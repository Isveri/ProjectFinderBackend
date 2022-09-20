package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class TokenAlreadySendException extends RuntimeException{

    private final String code = "15";

    public TokenAlreadySendException(String message){super(message);}
}
