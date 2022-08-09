package com.example.project.controllers;

import com.example.project.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @GetMapping("/test")
    public SseEmitter notifyUser() throws IOException {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId= user.getId();
        SseEmitter emitter = new SseEmitter(150000L);
        emitters.put(userId,emitter);
        emitter.onTimeout(emitter::complete);
        emitter.onCompletion(()-> emitters.remove(userId));
        return emitter;
    }
}
