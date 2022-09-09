package com.example.project.exceptions;

import lombok.Getter;

@Getter
public class ChatNotFoundException extends RuntimeException{
    private final String code = "13";

    public ChatNotFoundException(String message) {
        super(message);
    }
}
