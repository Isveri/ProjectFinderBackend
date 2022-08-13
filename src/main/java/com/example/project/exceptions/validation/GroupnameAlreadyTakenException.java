package com.example.project.exceptions.validation;

import lombok.Getter;

@Getter
public class GroupnameAlreadyTakenException extends RuntimeException{
    private final String code = "33";

    public GroupnameAlreadyTakenException(String message){super(message);}

}
