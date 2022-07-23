package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.model.MessageDTO;
import com.example.project.model.GroupRoomDTO;

import java.util.List;

public interface GroupRoomService {

    List<GroupRoomDTO> getAllGroups();

    List<GroupRoomDTO> getGroupsByGame(String game);

    List<GroupRoomDTO> getGroupsByGameCategory(Long gameId,Long categoryId);

    List<GroupRoomDTO> getGroupsByGameCategoryRole(Long gameId,Long categoryId,Long roleId);

    List<GroupRoomDTO> getGroupsByGameRole(Long gameId,Long roleId);

    void updateVisibility(Long groupId, boolean result);

    GroupRoomDTO getGroupByName(String name);

    GroupRoomDTO save(GroupRoomDTO groupRoomDTO);

    GroupRoomDTO getGroupById(Long id);

    GroupRoomDTO saveAndReturnDTO(GroupRoom groupRoom);

    GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomDTO groupRoomDTO);

    void deleteGroupRoomById(Long id);

    void addMessage(MessageDTO messageDTO);

    void deleteMessageById(Long commentId);
}

