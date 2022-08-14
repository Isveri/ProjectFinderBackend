package com.example.project.services;

import com.example.project.chat.model.Chat;
import com.example.project.exceptions.*;
import com.example.project.model.NotificationMsgDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.domain.*;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.model.JoinCodeDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.repositories.*;
import com.example.project.utils.RandomStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.project.utils.UserDetailsHelper.getCurrentUser;

@RequiredArgsConstructor
@Service

public class GroupRoomServiceImpl implements GroupRoomService {
    private final GroupRoomMapper groupRoomMapper;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final CategoryRepository categoryRepository;
    private final SseService sseService;

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
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found"));
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
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.findByName(name).orElseThrow(() -> new GroupNotFoundException("Group room not found")));
    }

    @Override
    public GroupRoomDTO save(GroupRoomDTO groupRoomDTO) {
        User currentUser = getCurrentUser();
        long id = currentUser.getId();
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found id:" + id));
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
                .orElseThrow(() -> new GroupNotFoundException("Group room not found, ID:" + id));
    }

    @Override
    public GroupRoomDTO saveAndReturnDTO(GroupRoom groupRoom) {
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
    }

    @Override
    public GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomDTO groupRoomDTO) {
        groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + id));
        GroupRoom groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        groupRoom.setId(id);
        return saveAndReturnDTO(groupRoom);
    }

    @Override
    public JoinCodeDTO generateJoinCode(Long groupId) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room no found id:" + groupId));
        User currentUser = getCurrentUser();

        if (groupRoom.getJoinCode() == null && groupRoom.getGroupLeader().getId().equals(currentUser.getId())) {
            String generatedCode = getUniqueCode();
            groupRoom.setJoinCode(generatedCode);
            groupRepository.save(groupRoom);
            return JoinCodeDTO.builder().code(generatedCode).build();
        } else {
            return null;
        }
    }

    private String getUniqueCode() {
        String generatedCode = RandomStringUtils.getRandomString();
        if (groupRepository.existsByJoinCode(generatedCode)) {
            return getUniqueCode();
        }
        return generatedCode;
    }

    public GroupRoomDTO joinGroupByCode(String code) {

        User currentUser = getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new UserNotFoundException("User not found id:" + currentUser.getId()));
        ;

        GroupRoom groupRoom = groupRepository.findGroupRoomByJoinCode(code);
        if (groupRoom == null) {
            throw new CodeDoesntExistException("Code doesnt fit any group");
        }
        if (user.getGroupRooms().contains(groupRoom)) {
            throw new AlreadyInGroupException("You already joined group " + groupRoom.getName());
        } else {
            user.getGroupRooms().add(groupRoom);
            userRepository.save(user);
            sseService.sendSseEventToUser(NotificationMsgDTO.builder().text(user.getUsername() + " joined group").isNegative(false).build(), groupRoom, null);
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRoom);
        }
    }

    @Override
    public GroupRoomDTO makePartyLeader(Long groupId, Long userId) {
        User currentUser = getCurrentUser();
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + groupId));
        User userToBeLeader = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));
        if (currentUser.getId().equals(groupRoom.getGroupLeader().getId())) {
            groupRoom.setGroupLeader(userToBeLeader);
            sseService.sendSseEventToUser(NotificationMsgDTO.builder().text(userToBeLeader.getUsername() + " is now group leader").isNegative(false).build(), groupRoom, null);
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
        }
        throw new NotGroupLeaderException("Not a group leader");
    }

    @Override
    public GroupRoomDTO removeUserFromGroup(Long groupId, Long userId) {
        User currentUser = getCurrentUser();
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + groupId));
        User userToRemove = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));

        if (currentUser.getId().equals(groupRoom.getGroupLeader().getId())) {
            groupRoom.getUsers().remove(userToRemove);
            userToRemove.getGroupRooms().remove(groupRoom);
            userRepository.save(userToRemove);
            sseService.sendSseEventToUser(NotificationMsgDTO.builder().text(userToRemove.getUsername() + " has been removed from group").type("REMOVED").isNegative(true).build(), groupRoom, userToRemove.getId());
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
        }
        throw new NotGroupLeaderException("Not a group leader");
    }

    @Override
    public void deleteGroupRoomById(Long id) {
        GroupRoom groupRoom = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + id));
        for (User user : groupRoom.getUsers()) {
            user.getGroupRooms().remove(groupRoom);
        }
        groupRepository.softDeleteById(id);
    }
}
