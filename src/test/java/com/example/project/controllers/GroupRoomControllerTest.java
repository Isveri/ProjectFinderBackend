package com.example.project.controllers;

import com.example.project.model.GroupRoomDTO;
import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.services.GroupRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

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
        mockMvc = MockMvcBuilders.standaloneSetup(groupRoomController).build();

    }

    @Test
    void getGroupByName() throws Exception {
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
    void getAllGroups() throws Exception {

        //given
        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
        when(groupRoomService.getAllGroups()).thenReturn(Collections.singletonList(groupRoomDTO));

        //when + then

        mockMvc.perform(get(baseUrl + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(groupRoomDTO));

        verify(groupRoomService, times(1)).getAllGroups();

    }

//    @Test
//    void getGroupsByGame() throws Exception {
//        //given
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        final String gameName = getGameDTOMock().getName();
//        when(groupRoomService.getGroupsByGame(any(String.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl + "/all/" + gameName))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService, times(1)).getGroupsByGame(gameName);
//    }

    @Test
    void createGroupRoom() throws Exception {
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
    void getDeletedGroups() throws Exception {
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
    void getGroupRoomById() throws Exception{
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
    void updateGroupRoom() throws Exception {
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
    void deleteGroupRoomById() throws Exception {

        //when + then
        mockMvc.perform(delete(baseUrl+"/"+groupId))
                .andExpect(status().isOk());

        verify(groupRoomService,times(1)).deleteGroupRoomById(groupId);
    }

    @Test
    void changeVisibility() throws Exception {
        //given
        final boolean visibilityValue = true;
        //when + then

        mockMvc.perform(patch(baseUrl+"/changeVisibility/"+groupId+"/"+visibilityValue))
                .andExpect(status().isOk());

        verify(groupRoomService,times(1)).updateVisibility(groupId,visibilityValue);
    }

//    @Test
//    void getGroupsByGameCategory() throws Exception {
//        //given
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        when(groupRoomService.getGroupsByGameCategory(any(Long.class),any(Long.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl+"/G&C/"+gameId+"/"+categoryId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService,times(1)).getGroupsByGameCategory(gameId,categoryId);
//    }

//    @Test
//    void getGroupsByGameInGameRole() throws Exception{
//        //given
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        when(groupRoomService.getGroupsByGameRole(any(Long.class),any(Long.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl+"/G&R/"+gameId+"/"+roleId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService,times(1)).getGroupsByGameRole(gameId,roleId);
//    }

//    @Test
//    void getGroupsByGameCategoryRole() throws Exception{
//        //given
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        when(groupRoomService.getGroupsByGameCategoryRole(any(Long.class),any(Long.class),any(Long.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl+"/G&C&R/"+gameId+"/"+categoryId+"/"+roleId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService,times(1)).getGroupsByGameCategoryRole(gameId,categoryId,roleId);
//    }

//    @Test
//    void getGroupsByGameCity() throws Exception {
//        //given
//        final String city = "Lublin";
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        when(groupRoomService.getGroupsByGameCity(any(Long.class),any(String.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl+"/g&cit/"+gameId+"/"+city))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService,times(1)).getGroupsByGameCity(gameId,city);
//    }

//    @Test
//    void getGroupsByGameCategoryCity() throws Exception {
//        //given
//        final String city = "Lublin";
//        final GroupRoomDTO groupRoomDTO = getGroupRoomDTOMock();
//        when(groupRoomService.getGroupsByGameCategoryCity(any(Long.class),any(Long.class),any(String.class))).thenReturn(Collections.singletonList(groupRoomDTO));
//
//        //when + then
//        mockMvc.perform(get(baseUrl+"/C&C/"+gameId+"/"+categoryId+"/"+city))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value(groupRoomDTO));
//
//        verify(groupRoomService,times(1)).getGroupsByGameCategoryCity(gameId,categoryId,city);
//    }

    @Test
    void generateJoinCode() throws Exception{
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
    void joinGroupByCode() throws Exception {
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
    void makeGroupRoomLeader() throws Exception {
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
    void removeUserFromGroup() throws Exception{
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