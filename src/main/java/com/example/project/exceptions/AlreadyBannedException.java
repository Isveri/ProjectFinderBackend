package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class AlreadyBannedException extends RuntimeException {

    private final String code = "7";

    public AlreadyBannedException(String message){super(message);}

}
