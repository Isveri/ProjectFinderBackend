package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException{
    private final String code = "16";

    public TokenExpiredException(String message){super(message);}
}
