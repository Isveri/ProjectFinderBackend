package com.example.project.services;

import com.example.project.chat.model.NotificationMsg;
import com.example.project.controllers.SseController;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SseServiceImpl implements SseService {
    @Override
    public void sendSseEventToUser(NotificationMsg notificationMsg){
        List<SseEmitter> sseEmitterListToRemove = new ArrayList<>();
        SseController.emitters.forEach((SseEmitter emitter)->{
            try{
                emitter.send(notificationMsg);
            }catch (IOException e){
                emitter.complete();
                sseEmitterListToRemove.add(emitter);
                e.printStackTrace();
            }
        });
        SseController.emitters.removeAll(sseEmitterListToRemove);
    }
}
