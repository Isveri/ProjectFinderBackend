package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class AccountBannedException extends RuntimeException{

    private final String code = "8";

    public AccountBannedException(String message){super(message);}
}
