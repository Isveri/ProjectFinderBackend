package com.example.project.services;

import com.example.project.domain.GroupRoom;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.chat.model.MessageDTO;
import com.example.project.model.GroupRoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupRoomService {

    List<GroupRoomDTO> getAllGroups();

    Page<GroupRoomDTO> getGroupsByGame(String game, Pageable pageable);

    List<GroupRoomDTO> getDeletedGroups();

    Page<GroupRoomDTO> getGroupsByGameCategory(Long gameId,Long categoryId, Pageable pageable);

    Page<GroupRoomDTO> getGroupsByGameCategoryRole(Long gameId,Long categoryId,Long roleId, Pageable pageable);

    Page<GroupRoomDTO> getGroupsByGameRole(Long gameId,Long roleId, Pageable pageable);

    Page<GroupRoomDTO> getGroupsByGameCity(Long gameId, String city, Pageable pageable);

    Page<GroupRoomDTO> getGroupsByGameCategoryCity(Long gameId,Long categoryId, String city, Pageable pageable);

    void updateVisibility(Long groupId, boolean result);

    GroupRoomDTO getGroupByName(String name);

    GroupRoomDTO save(GroupRoomDTO groupRoomDTO);

    GroupRoomDTO getGroupById(Long id);

    GroupRoomDTO saveAndReturnDTO(GroupRoom groupRoom);

    GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomUpdateDTO groupRoomUpdateDTO);

    JoinCodeDTO generateJoinCode(Long groupId);

    GroupRoomDTO joinGroupByCode(String code);

    GroupRoomDTO makePartyLeader(Long groupId,Long userId);

    GroupRoomDTO removeUserFromGroup(Long groupId,Long userId);

    void deleteGroupRoomById(Long id);
}

