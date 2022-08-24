package com.example.project.exceptions;

        import lombok.Getter;

@Getter
public class SomethingWrongException extends RuntimeException{
    private final String code = "99";

    public SomethingWrongException(String message){super(message);}

}
