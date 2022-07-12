package com.example.project.services;

import com.example.project.domain.Comment;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.NotFoundException;
import com.example.project.mappers.CommentMapper;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.model.CommentDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.repositories.CommentRepository;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class GroupRoomServiceImpl implements GroupRoomService {
    private final GroupRoomMapper groupRoomMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public List<GroupRoomDTO> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(groupRoom -> groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRoom))
                .collect(Collectors.toList());
    }

    @Override
    public GroupRoomDTO getGroupByName(String name) {
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.findByName(name).orElseThrow(() -> new NotFoundException("Group room not found")));
    }

    @Override
    public GroupRoomDTO save(GroupRoomDTO groupRoomDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id:"+id));
        GroupRoom groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        user.getGroupRooms().add(groupRoom);
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
    }

    @Override
    public GroupRoomDTO getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .orElseThrow(() -> new NotFoundException("Group room not found"));
    }

    @Override
    public GroupRoomDTO saveAndReturnDTO(GroupRoom groupRoom) {
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
    }

    @Override
    public GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomDTO groupRoomDTO) {
        groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group room not found id:"+id));
        GroupRoom groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        groupRoom.setId(id);
        return saveAndReturnDTO(groupRoom);
    }

    @Override
    public void deleteGroupRoomById(Long id) {
        GroupRoom groupRoom = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group room not found id:"+id));
        for (User user:groupRoom.getUsers()) {
            user.getGroupRooms().remove(groupRoom);
        }
        groupRoom.setUsers(null);
        groupRepository.deleteById(id);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id:"+id));

        Comment comment = commentMapper.mapCommentDTOTOComment(commentDTO);

        comment.setUser(user);
        comment.setGroupRoom(groupRepository.findById(commentDTO.getGroupId()).orElseThrow(() -> new NotFoundException("Group not found id:"+id)));

        commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
