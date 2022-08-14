package com.example.project.services;

import com.example.project.domain.User;
import com.example.project.model.NotificationMsgDTO;
import com.example.project.domain.GroupRoom;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Service
public class SseServiceImpl implements SseService {

    public static final Map<Long, SseEmitter> emitters = new HashMap<>();

    @Override
    public void sendSseEventToUser(NotificationMsgDTO notificationMsgDTO, GroupRoom groupRoom, Long modifiedUserId) {
        List<Long> usersId = new ArrayList<>();
        groupRoom.getUsers().forEach((user -> {
            usersId.add(user.getId());
        }));
        usersId.forEach((id) -> {
                    notificationMsgDTO.setType("");
                    sendMsgToEmitter(notificationMsgDTO, id);
                }
        );
        notificationMsgDTO.setType("REMOVED");
        notificationMsgDTO.setText("You have been removed from " + groupRoom.getName());
        notificationMsgDTO.setGroupId(groupRoom.getId());
        sendMsgToEmitter(notificationMsgDTO, modifiedUserId);
    }

    @Override
    public SseEmitter createEmitter() {
        User user = getCurrentUser();
        Long userId = user.getId();
        SseEmitter emitter = new SseEmitter(150000L);
        emitters.put(userId, emitter);
        emitter.onTimeout(emitter::complete);
        emitter.onCompletion(() -> emitters.remove(userId));
        return emitter;
    }

    private void sendMsgToEmitter(NotificationMsgDTO notificationMsgDTO, Long id) {
        SseEmitter emitter = emitters.get(id);
        try {
            if (emitter != null) {
                emitter.send(notificationMsgDTO);
            }
        } catch (IOException e) {
            emitter.complete();
            e.printStackTrace();
        }
    }
}
