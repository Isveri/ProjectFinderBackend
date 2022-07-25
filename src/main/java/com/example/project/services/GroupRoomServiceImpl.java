package com.example.project.services;

import com.example.project.domain.*;
import com.example.project.exceptions.NotFoundException;
import com.example.project.mappers.MessageMapper;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.model.JoinCodeDTO;
import com.example.project.model.MessageDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class GroupRoomServiceImpl implements GroupRoomService {
    private final GroupRoomMapper groupRoomMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ChatRepository chatRepository;

    private final CategoryRepository categoryRepository;

    private final GameRepository gameRepository;


    @Override
    public List<GroupRoomDTO> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGame(String game) {

        return groupRepository.findAllByGameNameAndOpenIsTrue(game)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateVisibility(Long groupId, boolean result) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group room not found"));
        groupRoom.setOpen(result);
        groupRepository.save(groupRoom);
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameCategory(Long gameId, Long categoryId) {
        return groupRepository.findAllByGameIdAndCategoryIdAndOpenIsTrue(gameId, categoryId)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameCategoryRole(Long gameId, Long categoryId, Long roleId) {
        return groupRepository.findAllByGameIdAndCategoryIdAndGameInGameRolesIdAndOpenIsTrue(gameId, categoryId, roleId)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameRole(Long gameId, Long roleId) {
        return groupRepository.findAllByGameIdAndGame_InGameRolesIdAndOpenIsTrue(gameId, roleId)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
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
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id:" + id));
        GroupRoom groupRoom = createGroupRoom(groupRoomDTO, user);
        groupRoom.setChat(createChat(groupRoom));
        Category category = categoryRepository.findByName(groupRoom.getCategory().getName());
        groupRoom.setCategory(category);
        groupRoom.setGame(category.getGame());
        groupRoom.setGroupLeader(user);
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
    }

    private GroupRoom createGroupRoom(GroupRoomDTO groupRoomDTO, User user) {
        GroupRoom groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        user.getGroupRooms().add(groupRoom);
        return groupRepository.save(groupRoom);
    }

    private Chat createChat(GroupRoom groupRoom) {
        Chat newChat = Chat.builder().groupRoom(groupRoom).build();
        return chatRepository.save(newChat);
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
        groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group room not found id:" + id));
        GroupRoom groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        groupRoom.setId(id);
        return saveAndReturnDTO(groupRoom);
    }

    @Override
    public JoinCodeDTO generateJoinCode(Long groupId) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException("Group room no found id:" + groupId));
        String generatedCode = getString();

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (groupRepository.findGroupRoomByJoinCode(generatedCode) != null) {
            generateJoinCode(groupId);

        }
        if (groupRoom.getJoinCode() == null && groupRoom.getGroupLeader().getId().equals(currentUser.getId())) {
            groupRoom.setJoinCode(generatedCode);
            groupRepository.save(groupRoom);
            return JoinCodeDTO.builder().code(generatedCode).build();
        } else {
            return null;
        }
    }

    private String getString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public GroupRoomDTO joinGroupByCode(String code){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new NotFoundException("User not found id:"+currentUser.getId()));;


        GroupRoom groupRoom = groupRepository.findGroupRoomByJoinCode(code);
        if(user.getGroupRooms().contains(groupRoom)){
            return null;
        }else{
            user.getGroupRooms().add(groupRoom);
            userRepository.save(user);
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRoom);
        }
    }

    @Override
    public void deleteGroupRoomById(Long id) {
        GroupRoom groupRoom = groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Group room not found id:" + id));
        for (User user : groupRoom.getUsers()) {
            user.getGroupRooms().remove(groupRoom);
        }
        groupRoom.setUsers(null);
        groupRepository.deleteById(id);
    }


    @Override
    public void addMessage(MessageDTO messageDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id:" + id));

        Message message = messageMapper.mapMessageDTOTOMessage(messageDTO);

        message.setUser(user);
        GroupRoom gr = groupRepository.findById(messageDTO.getGroupId()).orElseThrow(() -> new NotFoundException("Group not found id:" + id));
        Chat chat = chatRepository.findById(gr.getChat().getId()).orElseThrow(() -> new NotFoundException("Chat unavailable"));
        message.setChat(chat);
        messageRepository.save(message);

    }

    @Override
    public void deleteMessageById(Long commentId) {
        messageRepository.deleteById(commentId);
    }
}
