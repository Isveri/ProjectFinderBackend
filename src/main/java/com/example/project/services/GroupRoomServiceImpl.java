package com.example.project.services;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.CustomNotification.NotifType;
import com.example.project.chat.model.CustomNotificationDTO;
import com.example.project.chat.service.SseService;
import com.example.project.exceptions.*;
import com.example.project.mappers.TakenInGameRoleMapper;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.domain.*;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.model.JoinCodeDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.TakenInGameRoleDTO;
import com.example.project.repositories.*;
import com.example.project.utils.DataValidation;
import com.example.project.utils.RandomStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.project.utils.UserDetailsHelper.checkPrivilages;
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
    private final DataValidation dataValidation;
    private final TakenInGameRoleMapper takenInGameRoleMapper;

    private final TakenInGameRoleRepository takenInGameRoleRepository;

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
    public List<GroupRoomDTO> getDeletedGroups() {
        return groupRepository.findAllDeletedGroups().stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateVisibility(Long groupId, boolean result) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found"));
        if (checkPrivilages(groupRoom)) {
            groupRoom.setOpen(result);
            groupRepository.save(groupRoom);
        } else {
            throw new NotGroupLeaderException("You are not group leader or admin to change visibility");
        }
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
        return groupRepository.findAllByGameCategoryRole(gameId, categoryId, roleId)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameRole(Long gameId, Long roleId) {
        return groupRepository.findAllByGameRole(gameId, roleId)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameCity(Long gameId, String city) {
        return groupRepository.findAllByGameIdAndCityAndOpenIsTrue(gameId, city)
                .stream()
                .map(groupRoomMapper::mapGroupRoomToGroupRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRoomDTO> getGroupsByGameCategoryCity(Long gameId, Long categoryId, String city) {
        return groupRepository.findAllByGameIdAndCategoryIdAndCityAndOpenIsTrue(gameId, categoryId, city)
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
        GroupRoom groupRoom = prepareGroupRoom(groupRoomDTO, user);
        return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
    }

    private GroupRoom prepareGroupRoom(GroupRoomDTO groupRoomDTO, User user) {
        GroupRoom groupRoom = createGroupRoom(groupRoomDTO, user);
        groupRoom.setChat(createChat(groupRoom));
        Category category = categoryRepository.findByName(groupRoom.getCategory().getName());
        groupRoom.setCategory(category);
        groupRoom.setGame(category.getGame());
        groupRoom.setGroupLeader(user);
        groupRoom.setTakenInGameRoles(createTakenInGameRoles(groupRoomDTO, groupRoom, category));
        return groupRoom;
    }

    private List<TakenInGameRole> createTakenInGameRoles(GroupRoomDTO groupRoomDTO, GroupRoom groupRoom, Category category) {
        if (groupRoom.isInGameRolesActive()) {
            List<TakenInGameRole> list = new ArrayList<>();
            groupRoom.setMaxUsers(category.getBasicMaxUsers());
            if (!Objects.equals(groupRoom.getCategory().getName(), "SoloQ")) {
                for (InGameRole inGameRole : category.getGame().getInGameRoles()) {
                    TakenInGameRole takenInGameRole = new TakenInGameRole();
                    takenInGameRole.setInGameRole(inGameRole);
                    if (inGameRole.getId().equals(groupRoomDTO.getTakenInGameRoles().get(0).getInGameRole().getId())) {
                        takenInGameRole.setUser(getCurrentUser());
                    }
                    list.add(takenInGameRole);
                    takenInGameRoleRepository.save(takenInGameRole);
                }
            } else {
                for (int i = 0; i < groupRoomDTO.getTakenInGameRoles().size(); i++) {
                    TakenInGameRole takenInGameRole = takenInGameRoleMapper.mapTakenInGameRoleDTOToTakenInGameRole(groupRoomDTO.getTakenInGameRoles().get(i));
                    if (i == 0) {
                        takenInGameRole.setUser(getCurrentUser());
                    }
                    list.add(takenInGameRole);
                    takenInGameRoleRepository.save(takenInGameRole);
                }
            }
            return list;

        } else {
            return null;
        }
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
    public GroupRoomDTO updateGroupRoomByDTO(Long id, GroupRoomUpdateDTO groupRoomUpdateDTO) {
        GroupRoom groupRoom = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + id));
        // groupRoom = groupRoomMapper.mapGroupRoomDTOToGroupRoom(groupRoomDTO);
        //groupRoom.setId(id);
        //dataValidation.groupName(groupRoomUpdateDTO.getName(),groupRoom);
        dataValidation.userLimitUpdate(groupRoomUpdateDTO.getMaxUsers(),groupRoom);
        dataValidation.groupDesc(groupRoomUpdateDTO.getDescription());
        return saveAndReturnDTO(groupRoomMapper.updateGroupRoomFromGroupRoomUpdateDTO(groupRoomUpdateDTO, groupRoom));
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
        //TODO wywalic nulla i zrobic exception + w kontrolerze tylko response raz dac
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

        GroupRoom groupRoom = groupRepository.findGroupRoomByJoinCode(code);
        if (groupRoom == null) {
            throw new CodeDoesntExistException("Code doesnt fit any group");
        }
        if (user.getGroupRooms().contains(groupRoom)) {
            throw new AlreadyInGroupException("You already joined group " + groupRoom.getName());
        } else {
            user.getGroupRooms().add(groupRoom);
            if (groupRoom.isInGameRolesActive()) {
                groupRoom.getTakenInGameRoles().stream().filter((takenInGameRole -> takenInGameRole.getUser() == null)).findFirst().orElseThrow(null).setUser(currentUser);
            }
            userRepository.save(user);
            sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(user.getUsername() + " joined ").type(NotifType.INFO).build(), groupRoom, null);
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRoom);
        }
    }

    @Override
    public GroupRoomDTO makePartyLeader(Long groupId, Long userId) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + groupId));
        User userToBeLeader = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));
        if (checkPrivilages(groupRoom)) {
            groupRoom.setGroupLeader(userToBeLeader);
            sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(userToBeLeader.getUsername() + " is now group leader").type(NotifType.INFO).build(), groupRoom, null);
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
        }
        throw new NotGroupLeaderException("Not a group leader");
    }

    @Override
    public GroupRoomDTO removeUserFromGroup(Long groupId, Long userId) {
        GroupRoom groupRoom = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + groupId));
        User userToRemove = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found id:" + userId));

        if (checkPrivilages(groupRoom)) {
            groupRoom.getUsers().remove(userToRemove);
            if (groupRoom.isInGameRolesActive()) {
                groupRoom.getTakenInGameRoles().stream().filter((takenInGameRole) -> userToRemove.equals(takenInGameRole.getUser())).findAny().orElse(new TakenInGameRole()).setUser(null);
            }
            userToRemove.getGroupRooms().remove(groupRoom);
            userRepository.save(userToRemove);
            sseService.sendSseEventToUser(CustomNotificationDTO.builder().msg(userToRemove.getUsername() + " has been removed").type(NotifType.REMOVED).build(), groupRoom, userToRemove.getId());
            return groupRoomMapper.mapGroupRoomToGroupRoomDTO(groupRepository.save(groupRoom));
        }
        throw new NotGroupLeaderException("Not a group leader");
    }

    @Override
    public void deleteGroupRoomById(Long id) {
        GroupRoom groupRoom = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException("Group room not found id:" + id));
        if (checkPrivilages(groupRoom)) {
            for (User user : groupRoom.getUsers()) {
                user.getGroupRooms().remove(groupRoom);
            }
            groupRepository.softDeleteById(id);
        } else {
            throw new NotGroupLeaderException("You are not a leader of this group or admin");

        }
    }
}
