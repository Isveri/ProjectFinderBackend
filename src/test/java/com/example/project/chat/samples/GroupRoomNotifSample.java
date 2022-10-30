package com.example.project.chat.samples;

import com.example.project.model.GroupNotifInfoDTO;

public class GroupRoomNotifSample {

    public static GroupNotifInfoDTO getGroupNotifInfoDTOMock(){
        return GroupNotifInfoDTO.builder()
                .name("mock").id(1L).build();
    }
}
