package com.example.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notify")
public class SseController {

    public static final List<SseEmitter> emitters = Collections.synchronizedList(new ArrayList<>());

    @GetMapping("/test")
    public SseEmitter notifyUser() throws IOException {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(()-> emitters.remove(emitter));
        return emitter;
    }
}
