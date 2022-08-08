package com.example.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/notify")
public class SseController {

    public static final Map<Long,SseEmitter> emitters = new HashMap<>();


    @GetMapping("/test/{userId}")
    public SseEmitter notifyUser(@PathVariable Long userId) throws IOException {
        SseEmitter emitter = new SseEmitter(150000L);
        emitters.put(userId,emitter);
        emitter.onTimeout(emitter::complete);
        emitter.onCompletion(()-> emitters.remove(userId));
        return emitter;
    }
}
