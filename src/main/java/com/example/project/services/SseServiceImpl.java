package com.example.project.services;

import com.example.project.chat.model.NotificationMsg;
import com.example.project.controllers.SseController;
import com.example.project.domain.GroupRoom;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SseServiceImpl implements SseService {
    @Override
    public void sendSseEventToUser(NotificationMsg notificationMsg, GroupRoom groupRoom,Long modifiedUserId){
        List<Long> usersId = new ArrayList<>();
        groupRoom.getUsers().forEach((user -> {
            usersId.add(user.getId());
        }));
        usersId.forEach((id)->{
                    notificationMsg.setType("");
                    sendMsgToEmitter(notificationMsg,id);
                }
        );
        notificationMsg.setType("REMOVED");
            sendMsgToEmitter(notificationMsg,modifiedUserId);
    }

    private void sendMsgToEmitter(NotificationMsg notificationMsg,  Long id) {
        SseEmitter emitter = SseController.emitters.get(id);
        try{
            if(emitter!=null) {
                emitter.send(notificationMsg);
            }
        }catch (IOException e){
            emitter.complete();
            e.printStackTrace();
        }
    }
}
