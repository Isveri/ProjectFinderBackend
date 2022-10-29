package com.example.project.services;

import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.service.SseService;
import com.example.project.domain.Category;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.exceptions.AlreadyInGroupException;
import com.example.project.exceptions.AlreadyReportedException;
import com.example.project.exceptions.CodeDoesntExistException;
import com.example.project.exceptions.NotGroupLeaderException;
import com.example.project.mappers.GroupRoomMapper;
import com.example.project.mappers.TakenInGameRoleMapper;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.repositories.CategoryRepository;
import com.example.project.repositories.GroupRepository;
import com.example.project.repositories.TakenInGameRoleRepository;
import com.example.project.repositories.UserRepository;
import com.example.project.utils.DataValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static com.example.project.samples.CategorySample.getCategoryMock;
import static com.example.project.samples.GroupRoomSample.*;
import static com.example.project.samples.UserMockSample.getCurrentUserMock;
import static com.example.project.samples.UserMockSample.getUserMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GroupRoomServiceImplTest {

    @Mock
    private GroupRoomMapper groupRoomMapper;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SseService sseService;
    @Mock
    private DataValidation dataValidation;
    @Mock
    private TakenInGameRoleMapper takenInGameRoleMapper;
    @Mock
    private TakenInGameRoleRepository takenInGameRoleRepository;

    private GroupRoomService groupRoomService;

    private GroupRoom gr;
    private GroupRoomDTO groupRoomDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupRoomService = new GroupRoomServiceImpl(groupRoomMapper,groupRepository,userRepository,chatRepository,categoryRepository,sseService,dataValidation,
                takenInGameRoleMapper,takenInGameRoleRepository);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(getCurrentUserMock());

         groupRoomDTO = getGroupRoomDTOMock();
         gr = getGroupRoomMock();
    }

    @Test
    void getAllGroups() {
        //given
        when(groupRepository.findAll()).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getAllGroups();

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());

        verify(groupRepository,times(1)).findAll();
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void getGroupsByGame() {
        //given
        String gameName = "CSGO";
        when(groupRepository.findAllByGameNameAndOpenIsTrue(any(String.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(gr)).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGame(gameName);


        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameNameAndOpenIsTrue(gameName);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);

    }

    @Test
    void getDeletedGroups() {
        //given
        when(groupRepository.findAllDeletedGroups()).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(gr)).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getDeletedGroups();

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository,times(1)).findAllDeletedGroups();
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void updateVisibility() {
        //given
        Long groupId = gr.getId();
        boolean value = true;
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));

        //when
        groupRoomService.updateVisibility(groupId,value);

        //then
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(1)).save(any());
    }

    @Test
    void should_throw_not_group_leader_exception() {
        //given
        Long groupId = gr.getId();
        gr.setGroupLeader(getUserMock());
        boolean value = true;
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));

        //when
        Exception exception = assertThrows(NotGroupLeaderException.class, () ->groupRoomService.updateVisibility(groupId,value));

        //then
        assertNotNull(exception);
        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(0)).save(any());
    }

    @Test
    void getGroupsByGameCategory() {
        //given
        Long gameId = 1L;
        Long categoryId = 1L;
        when(groupRepository.findAllByGameIdAndCategoryIdAndOpenIsTrue(any(Long.class), any(Long.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGameCategory(gameId, categoryId);

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameIdAndCategoryIdAndOpenIsTrue(gameId, categoryId);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);

    }

    @Test
    void getGroupsByGameCategoryRole() {
        //given
        Long gameId = 1L;
        Long categoryId = 1L;
        Long roleId = 1L;
        when(groupRepository.findAllByGameCategoryRole(any(Long.class),any(Long.class), any(Long.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGameCategoryRole(gameId, categoryId,roleId);

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameCategoryRole(gameId, categoryId, roleId);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void getGroupsByGameRole() {
        //given
        Long gameId = 1L;
        Long roleId = 1L;
        when(groupRepository.findAllByGameRole(any(Long.class), any(Long.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGameRole(gameId, roleId);

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameRole(gameId, roleId);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void getGroupsByGameCity() {
        //given
        Long gameId = 1L;
        String city = "Lublin";
        when(groupRepository.findAllByGameIdAndCityAndOpenIsTrue(any(Long.class), any(String.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGameCity(gameId, city);

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameIdAndCityAndOpenIsTrue(gameId, city);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);

    }

    @Test
    void getGroupsByGameCategoryCity() {
        //given
        Long gameId = 1L;
        Long categoryId = 1L;
        String city = "Lublin";
        when(groupRepository.findAllByGameIdAndCategoryIdAndCityAndOpenIsTrue(any(Long.class),any(Long.class), any(String.class))).thenReturn(Collections.singletonList(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        List<GroupRoomDTO> result = groupRoomService.getGroupsByGameCategoryCity(gameId,categoryId, city);

        //then
        assertThat(result.get(0).getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findAllByGameIdAndCategoryIdAndCityAndOpenIsTrue(gameId,categoryId, city);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void getGroupByName() {
        //given
        String name = "Group Mock";
        when(groupRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        GroupRoomDTO result = groupRoomService.getGroupByName(name);

        //then
        assertThat(result.getName()).isEqualTo(gr.getName());
        verify(groupRepository, times(1)).findByName(name);
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void save() {
        //given
        Long userId = 2L;
        Category category = getCategoryMock();
        User currentUser = getCurrentUserMock();
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);
        when(groupRoomMapper.mapGroupRoomDTOToGroupRoom(any(GroupRoomDTO.class))).thenReturn(gr);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.save(any(GroupRoom.class))).thenReturn(gr);
        when(categoryRepository.findByName(any(String.class))).thenReturn(category);

        //when
        GroupRoomDTO result = groupRoomService.save(groupRoomDTO);

        //then
        assertThat(result.getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(2)).save(any());
        verify(userRepository, times(1)).findById(userId);
        verify(categoryRepository, times(1)).findByName(category.getName());
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRoomMapper, times(1)).mapGroupRoomDTOToGroupRoom(groupRoomDTO);
    }

    @Test
    void getGroupById() {
        //given
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        GroupRoomDTO result = groupRoomService.getGroupById(gr.getId());

        //then
        assertThat(result.getId()).isEqualTo(gr.getId());
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void saveAndReturnDTO() {
        //given
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);
        when(groupRepository.save(any(GroupRoom.class))).thenReturn(gr);

        //when
        GroupRoomDTO result = groupRoomService.saveAndReturnDTO(gr);

        //then
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRepository,times(1)).save(gr);
    }

    @Test
    void updateGroupRoomByDTO() {
        //given
        GroupRoomUpdateDTO gru = getGroupRoomUpdateDTOMock();
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(groupRoomMapper.updateGroupRoomFromGroupRoomUpdateDTO(any(GroupRoomUpdateDTO.class), any(GroupRoom.class))).thenReturn(gr);

        //when
        GroupRoomDTO result = groupRoomService.updateGroupRoomByDTO(gr.getId(), gru);

        //then
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRoomMapper,times(1)).updateGroupRoomFromGroupRoomUpdateDTO(gru, gr);
        verify(dataValidation, times(1)).userLimitUpdate(gru.getMaxUsers(), gr);
        verify(dataValidation,times(1)).groupDesc(gru.getDescription());
    }


    @Test
    void should_generate_and_return_join_code() {
        //given
        gr.setGroupLeader(getCurrentUserMock());
        gr.setJoinCode(null);
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(groupRepository.existsByJoinCode(any(String.class))).thenReturn(false);
        //when
        JoinCodeDTO result = groupRoomService.generateJoinCode(gr.getId());

        //then
        assertNotNull(result);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRepository, times(1)).existsByJoinCode(any(String.class));
        verify(groupRepository, times(1)).save(gr);
    }

    //TODO POPRAWIC TEST PO ZMIANIE METODY ZEBY WYRZUCALA EXCEPTION
    @Test
    void should_not_generate_and_return_join_code() {
        //given
        gr.setJoinCode("already exist");
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(groupRepository.existsByJoinCode(any(String.class))).thenReturn(false);

//        //when
//        Exception exception = assertThrows(CodeAlreadyExist.class, () ->groupRoomService.generateJoinCode(gr.getId()));
//
//        //then
//        assertNotNull(exception);

        JoinCodeDTO result = groupRoomService.generateJoinCode(gr.getId());

        //then
        assertNull(result);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRepository, times(0)).existsByJoinCode(any(String.class));
        verify(groupRepository, times(0)).save(gr);
    }


    @Test
    void should_join_group_and_return_group_room_DTO() {
        //given
        User currentUser = getCurrentUserMock();
        String joinCode = "sersad23";
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.findGroupRoomByJoinCode(any(String.class))).thenReturn(gr);
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        GroupRoomDTO result = groupRoomService.joinGroupByCode(joinCode);

        //then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(groupRepository, times(1)).findGroupRoomByJoinCode(joinCode);
        verify(userRepository, times(1)).save(currentUser);
        verify(sseService,times(1)).sendSseEventToUser(any(),any(),any());
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void should_throw_code_doesnt_exist_exception() {
        //given
        User currentUser = getCurrentUserMock();
        String joinCode = "sersad23";
        gr = null;
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.findGroupRoomByJoinCode(any(String.class))).thenReturn(gr);
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        Exception exception = assertThrows(CodeDoesntExistException.class, () ->groupRoomService.joinGroupByCode(joinCode));

        //then
        assertNotNull(exception);
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(groupRepository, times(1)).findGroupRoomByJoinCode(joinCode);
        verify(userRepository, times(0)).save(currentUser);
        verify(sseService,times(0)).sendSseEventToUser(any(),any(),any());
        verify(groupRoomMapper, times(0)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void should_throw_already_in_group_exception() {
        //given
        User currentUser = getCurrentUserMock();
        String joinCode = "sersad23";
        currentUser.getGroupRooms().add(gr);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.findGroupRoomByJoinCode(any(String.class))).thenReturn(gr);
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        Exception exception = assertThrows(AlreadyInGroupException.class, () ->groupRoomService.joinGroupByCode(joinCode));

        //then
        assertNotNull(exception);
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(groupRepository, times(1)).findGroupRoomByJoinCode(joinCode);
        verify(userRepository, times(0)).save(currentUser);
        verify(sseService,times(0)).sendSseEventToUser(any(),any(),any());
        verify(groupRoomMapper, times(0)).mapGroupRoomToGroupRoomDTO(gr);
    }

    @Test
    void makePartyLeader() {
        //given
        User currentUser = getCurrentUserMock();
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.save(any(GroupRoom.class))).thenReturn(gr);
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        GroupRoomDTO result = groupRoomService.makePartyLeader(gr.getId(), currentUser.getId());

        //then
        assertNotNull(result);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(sseService, times(1)).sendSseEventToUser(any(), any(), any());
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRepository, times(1)).save(gr);
    }
    @Test
    void should_throw_not_group_leader_exception_when_make_leader() {
        //given
        User currentUser = getCurrentUserMock();
        gr.setGroupLeader(getUserMock());
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));

        //when
        Exception exception = assertThrows(NotGroupLeaderException.class, () ->groupRoomService.makePartyLeader(gr.getId(), currentUser.getId()));

        assertNotNull(exception);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(sseService, times(0)).sendSseEventToUser(any(), any(), any());
        verify(groupRoomMapper, times(0)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRepository, times(0)).save(gr);
    }


    @Test
    void should_remove_user_from_group_and_return_dto() {
        //given
        User currentUser = getCurrentUserMock();
        gr.setUsers(new ArrayList<>(Arrays.asList(currentUser)));
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));
        when(groupRepository.save(any(GroupRoom.class))).thenReturn(gr);
        when(groupRoomMapper.mapGroupRoomToGroupRoomDTO(any(GroupRoom.class))).thenReturn(groupRoomDTO);

        //when
        GroupRoomDTO result = groupRoomService.removeUserFromGroup(gr.getId(), currentUser.getId());

        //then
        assertNotNull(result);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(sseService, times(1)).sendSseEventToUser(any(), any(), any());
        verify(groupRoomMapper, times(1)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRepository, times(1)).save(gr);
        verify(userRepository, times(1)).save(currentUser);
    }

    @Test
    void should_throw_not_group_leader_exception_when_removing_from_group() {
        //given
        User currentUser = getCurrentUserMock();
        gr.setGroupLeader(getUserMock());
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(currentUser));

        //when
        Exception exception = assertThrows(NotGroupLeaderException.class, () ->groupRoomService.removeUserFromGroup(gr.getId(), currentUser.getId()));

        //then
        assertNotNull(exception);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(userRepository, times(1)).findById(currentUser.getId());
        verify(sseService, times(0)).sendSseEventToUser(any(), any(), any());
        verify(groupRoomMapper, times(0)).mapGroupRoomToGroupRoomDTO(gr);
        verify(groupRepository, times(0)).save(gr);
        verify(userRepository, times(0)).save(currentUser);
    }


    @Test
    void should_soft_delete_group_by_id() {
        //given
        gr.setUsers(new ArrayList<>(Arrays.asList(getCurrentUserMock())));
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));

        //when
        groupRoomService.deleteGroupRoomById(gr.getId());

        //then
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRepository,times(1)).softDeleteById(gr.getId());

    }

    @Test
    void soft_delete_should_throw_not_group_leader_exception() {
        //given
        gr.setGroupLeader(getUserMock());
        when(groupRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(gr));

        //when
        Exception exception = assertThrows(NotGroupLeaderException.class, () ->groupRoomService.deleteGroupRoomById(gr.getId()));

        //then
        assertNotNull(exception);
        verify(groupRepository, times(1)).findById(gr.getId());
        verify(groupRepository,times(0)).softDeleteById(gr.getId());

    }

}