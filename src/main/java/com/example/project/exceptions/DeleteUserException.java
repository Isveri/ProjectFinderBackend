package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class DeleteUserException extends RuntimeException{
    private final String code = "10";
    public DeleteUserException(String message){super(message);}
}
