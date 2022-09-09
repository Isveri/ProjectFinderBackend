package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class AlreadyFriendException extends RuntimeException{

    private final String code = "12";

    public AlreadyFriendException(String message){super(message);}
}
