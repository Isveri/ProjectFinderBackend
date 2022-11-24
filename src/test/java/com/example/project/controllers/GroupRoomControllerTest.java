package com.example.project.controllers;

import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.services.GroupRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static com.example.project.converters.Converter.convertObjectToJsonBytes;
import static com.example.project.samples.GameSample.getGameDTOMock;
import static com.example.project.samples.GroupRoomSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupRoomControllerTest {


    @Mock
    private GroupRoomService groupRoomService;
    private MockMvc mockMvc;
    private GroupRoomController groupRoomController;
    private static final String baseUrl = "/api/v1/groups";

    private static final Long groupId = 1L;
    private static final Long userId = 1L;
    private static final Long gameId = 1L;
    private static final Long categoryId = 1L;
    private static final Long roleId = 1L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupRoomController = new GroupRoomController(groupRoomService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupRoomController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

    }

    @Test
    void should_return_group_by_name() throws Exception {
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        final String groupName = groupRoomDTO.getName();
        when(groupRoomService.getGroupByName(any(String.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(get(baseUrl).param("name", groupName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()))
                .andExpect(jsonPath("groupLeader").value(groupRoomDTO.getGroupLeader()))
                .andExpect(jsonPath("game").value(groupRoomDTO.getGame()));

        verify(groupRoomService, times(1)).getGroupByName(groupName);
    }

    @Test
    void should_return_all_groups() throws Exception {

        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.getAllGroups()).thenReturn(Collections.singletonList(groupRoomDTO));

        //when + then

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(groupRoomDTO));

        verify(groupRoomService, times(1)).getAllGroups();

    }

    @Test
    void should_return_group_by_game_name() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        final String gameName = getGameDTOMock().getName();
        when(groupRoomService.getGroupsByGame(any(String.class),any())).thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl + "/all/" + gameName +"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService, times(1)).getGroupsByGame(gameName,pageRequest);
    }

    @Test
    void should_create_group_room() throws Exception {
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        byte[] content = convertObjectToJsonBytes(groupRoomDTO);
        when(groupRoomService.save(any(GroupRoomDTO.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(post(baseUrl + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("groupLeader").value(groupRoomDTO.getGroupLeader()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()));
        verify(groupRoomService, times(1)).save(groupRoomDTO);
    }

    @Test
    void should_return_deleted_groups() throws Exception {
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.getDeletedGroups()).thenReturn(Collections.singletonList(groupRoomDTO));

        //when + then
        mockMvc.perform(get(baseUrl + "/deleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(groupRoomDTO));

        verify(groupRoomService, times(1)).getDeletedGroups();
    }

    @Test
    void should_return_group_by_id() throws Exception{
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.getGroupById(any(Long.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(get(baseUrl + "/" + groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("groupLeader").value(groupRoomDTO.getGroupLeader()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()));

        verify(groupRoomService,times(1)).getGroupById(groupId);
    }

    @Test
    void should_update_group_room() throws Exception {
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        final GroupRoomUpdateDTO groupRoomUpdateDTO = getGroupRoomUpdateDTOMock();
        byte[] content = convertObjectToJsonBytes(groupRoomUpdateDTO);
        when(groupRoomService.updateGroupRoomByDTO(any(Long.class),any(GroupRoomUpdateDTO.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(put(baseUrl+"/"+groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()))
                .andExpect(jsonPath("maxUsers").value(groupRoomDTO.getMaxUsers()))
                .andExpect(jsonPath("description").value(groupRoomDTO.getDescription()));

        verify(groupRoomService,times(1)).updateGroupRoomByDTO(groupId,groupRoomUpdateDTO);
    }

    @Test
    void should_delete_group_room_by_id() throws Exception {

        //when + then
        mockMvc.perform(delete(baseUrl+"/"+groupId))
                .andExpect(status().isOk());

        verify(groupRoomService,times(1)).deleteGroupRoomById(groupId);
    }

    @Test
    void should_change_visibility() throws Exception {
        //given
        final boolean visibilityValue = true;
        //when + then

        mockMvc.perform(patch(baseUrl+"/changeVisibility/"+groupId+"/"+visibilityValue))
                .andExpect(status().isOk());

        verify(groupRoomService,times(1)).updateVisibility(groupId,visibilityValue);
    }

    @Test
    void should_return_groups_by_game_and_category() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        when(groupRoomService.getGroupsByGameCategory(any(Long.class),any(Long.class),any(Pageable.class))).thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl+"/G&C/"+gameId+"/"+categoryId+"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService,times(1)).getGroupsByGameCategory(gameId,categoryId,pageRequest);
    }

    @Test
    void should_return_groups_by_game_and_role() throws Exception{
        //given
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        when(groupRoomService.getGroupsByGameRole(any(Long.class),any(Long.class),any(Pageable.class))).thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl+"/G&R/"+gameId+"/"+roleId+"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService,times(1)).getGroupsByGameRole(gameId,roleId,pageRequest);
    }

    @Test
    void should_return_groups_by_game_category_and_role() throws Exception{
        //given
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        when(groupRoomService.getGroupsByGameCategoryRole(any(Long.class),any(Long.class),any(Long.class),any(Pageable.class)))
                .thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl+"/G&C&R/"+gameId+"/"+categoryId+"/"+roleId+"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService,times(1)).getGroupsByGameCategoryRole(gameId,categoryId,roleId,pageRequest);
    }

    @Test
    void should_return_groups_by_game_and_city() throws Exception {
        //given
        final String city = "Lublin";
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        when(groupRoomService.getGroupsByGameCity(any(Long.class),any(String.class),any(Pageable.class))).thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl+"/g&cit/"+gameId+"/"+city+"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService,times(1)).getGroupsByGameCity(gameId,city,pageRequest);
    }

    @Test
    void should_return_groups_by_game_category_and_city() throws Exception {
        //given
        final String city = "Lublin";
        PageRequest pageRequest = PageRequest.of(1,1);
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        List<GroupRoomDTO> groups = Collections.singletonList(groupRoomDTO);
        Page<GroupRoomDTO> page = new PageImpl<>(groups,pageRequest,groups.size());
        when(groupRoomService.getGroupsByGameCategoryCity(any(Long.class),any(Long.class),any(String.class),any(Pageable.class))).thenReturn(page);

        //when + then
        mockMvc.perform(get(baseUrl+"/C&C/"+gameId+"/"+categoryId+"/"+city+"?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(groupRoomDTO));

        verify(groupRoomService,times(1)).getGroupsByGameCategoryCity(gameId,categoryId,city,pageRequest);
    }

    @Test
    void should_generate_join_code() throws Exception{
        //given
        final JoinCodeDTO joinCodeDTO = getJoinCodeDTOMock();
        when(groupRoomService.generateJoinCode(any(Long.class))).thenReturn(joinCodeDTO);

        //when + then
        mockMvc.perform(get(baseUrl+"/generateCode/"+groupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(joinCodeDTO.getCode()));

        verify(groupRoomService, times(1)).generateJoinCode(groupId);
    }

    @Test
    void should_add_user_to_group_by_code() throws Exception {
        // given
        final String code = "Heh";
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.joinGroupByCode(any(String.class))).thenReturn(groupRoomDTO);

        // when + then
        mockMvc.perform(patch(baseUrl+"/joinByCode/"+code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()))
                .andExpect(jsonPath("maxUsers").value(groupRoomDTO.getMaxUsers()))
                .andExpect(jsonPath("description").value(groupRoomDTO.getDescription()))
                .andExpect(jsonPath("users[0]").value(groupRoomDTO.getUsers().get(0)));

        verify(groupRoomService,times(1)).joinGroupByCode(code);
    }

    @Test
    void should_make_groupLeader_by_id() throws Exception {
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.makePartyLeader(any(Long.class), any(Long.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(patch(baseUrl+"/makeLeader/"+groupId+"/"+userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()))
                .andExpect(jsonPath("groupLeader").value(groupRoomDTO.getGroupLeader()))
                .andExpect(jsonPath("users[0]").value(groupRoomDTO.getUsers().get(0)));

        verify(groupRoomService,times(1)).makePartyLeader(groupId,userId);
    }

    @Test
    void should_remove_user_from_group() throws Exception{
        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.removeUserFromGroup(any(Long.class), any(Long.class))).thenReturn(groupRoomDTO);

        //when + then
        mockMvc.perform(patch(baseUrl+"/removeUser/"+groupId+"/"+userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(groupRoomDTO.getId()))
                .andExpect(jsonPath("name").value(groupRoomDTO.getName()))
                .andExpect(jsonPath("groupLeader").value(groupRoomDTO.getGroupLeader()))
                .andExpect(jsonPath("users[0]").value(groupRoomDTO.getUsers().get(0)));

        verify(groupRoomService,times(1)).removeUserFromGroup(groupId,userId);


    }
}