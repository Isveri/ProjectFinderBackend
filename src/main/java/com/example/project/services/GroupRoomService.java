package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.model.CommentDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GroupRoomService {

    List<GroupRoomDTO> getAllGroups();

    GroupRoomDTO getGroupByName(String name);

    GroupRoomDTO save(GroupRoomDTO groupRoomDTO);

    GroupRoomDTO getGroupById(Long id);

    GroupRoomDTO saveAndReturnDTO(GroupRoom groupRoom);

    GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomDTO groupRoomDTO);

    void deleteGroupRoomById(Long id);

    void addComment(CommentDTO commentDTO);

    void deleteCommentById(Long commentId);
}

