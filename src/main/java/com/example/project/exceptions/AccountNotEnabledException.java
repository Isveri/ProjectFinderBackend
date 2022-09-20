package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class AccountNotEnabledException extends RuntimeException{

    private final String code = "17";

    public AccountNotEnabledException(String message){
        super(message);}
}
