package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class EmptyMessageException extends RuntimeException{
    private final String code = "14";

    public EmptyMessageException(String message){super(message);}
}
