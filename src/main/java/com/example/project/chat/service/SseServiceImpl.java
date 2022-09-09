package com.example.project.chat.service;

import com.example.project.chat.mappers.NotificationMapper;
import com.example.project.chat.model.CustomNotification;
import com.example.project.chat.model.CustomNotification.NotifType;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.repositories.NotificationRepository;
import com.example.project.domain.User;
import com.example.project.domain.GroupRoom;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.project.chat.model.CustomNotification.NotifType.REMOVED;
import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Service
public class SseServiceImpl implements SseService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    private final GroupRoomMapper groupRoomMapper;
    public static final Map<Long, SseEmitter> emitters = new HashMap<>();


    @Override
    public void sendSseEventToUser(CustomNotificationDTO customNotificationDTO, GroupRoom groupRoom, Long modifiedUserId) {
        List<Long> usersId = new ArrayList<>();
        groupRoom.getUsers().forEach((user -> {
                usersId.add(user.getId());
                customNotificationDTO.setGroupRoom(groupRoomMapper.mapGroupRoomToGroupNotifInfoDTO(groupRoom));
                CustomNotification customNotification = notificationMapper.mapCustomNotificationDTOToCustomNotification(customNotificationDTO);
                customNotification.setUser(user);
                customNotification.setGroupRoom(groupRoom);
                notificationRepository.save(customNotification);
        }));
        usersId.forEach((id) -> {
                    sendMsgToEmitter(customNotificationDTO, id);
                }
        );
        if (modifiedUserId != null) {
            customNotificationDTO.setRemovedUserId(modifiedUserId);
            customNotificationDTO.setGroupRoom(groupRoomMapper.mapGroupRoomToGroupNotifInfoDTO(groupRoom));
            CustomNotification customNotification = notificationMapper.mapCustomNotificationDTOToCustomNotification(customNotificationDTO);
            customNotification.setUser((User) userRepository.findById(modifiedUserId).orElseThrow());
            customNotification.setGroupRoom(groupRoom);
            notificationRepository.save(customNotification);
            sendMsgToEmitter(customNotificationDTO, modifiedUserId);
        }
    }

    @Override
    public void sendSseFriendEvent(CustomNotificationDTO customNotificationDTO, Long userId) {
        sendMsgToEmitter(customNotificationDTO,userId);
    }

    @Override
    public SseEmitter createEmitter() {
        User user = getCurrentUser();
        Long userId = user.getId();
        SseEmitter emitter = new SseEmitter(7_200_000L);
        emitters.put(userId, emitter);
        emitter.onTimeout(emitter::complete);
        emitter.onCompletion(() -> emitters.remove(userId));
        return emitter;
    }


    private void sendMsgToEmitter(CustomNotificationDTO customNotificationDTO, Long id) {
        SseEmitter emitter = emitters.get(id);
        try {
            if (emitter != null) {
                emitter.send(customNotificationDTO);
            }
        } catch (IOException e) {
            emitter.complete();
            e.printStackTrace();
        }
    }
}
