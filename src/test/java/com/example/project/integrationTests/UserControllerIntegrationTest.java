package com.example.project.integrationTests;

import com.example.project.converters.Converter;
import com.example.project.domain.Category;
import com.example.project.domain.Game;
import com.example.project.domain.GroupRoom;
import com.example.project.domain.User;
import com.example.project.integrationTests.bootData.BootData;
import com.example.project.model.ReportDTO;
import com.example.project.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.example.project.samples.ReportMockSample.getReportDTOMock;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private BootData bootData;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final String baseUrl = "/api/v1/users";

    private String token;

    private User user;
    private List<User> users;

    @BeforeEach
    @Rollback
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        users = bootData.createUsers();
        users = bootData.setUserPrivilages(users);
        user = users.get(0);
        token = jwtTokenUtil.generateAccessToken(user);
        // given depends on BootData class in bootData package above test
    }


    @Test
    public void should_return_unauthorized() throws Exception {
        //given
        token = "eyJdJIUzUxMiJ9.eyJzdWIiOiJFdmkiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlzcyI6ImhlaCIsImlhdCI6MTY2ODcyMTI4OSwiZXhwIjoxNjY5MzI2MDg5fQ.eQk8XlnOUYUMNuGTdNth4qXXxnaANa3kZNXxPXermfQOSc44xfiDlGKIxppwIpoqZA-AfMGRKmkSjn9RAXsDUw";

        //when + then
        mockMvc.perform(get(baseUrl + "/" + user.getUsername())
                .header("Authorization", "Bearer " + token)).andExpect(status().isUnauthorized());

    }

    @WithMockUser("Test")
    @Test
    public void should_return_users_as_userDTO_in_list() throws Exception {
        //given
        int numberOfUsersInRepo = 6;

        //when + then
        mockMvc.perform(get(baseUrl + "/all")
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(numberOfUsersInRepo)))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()));
    }

    @Test
    public void should_return_user_by_token_as_UserProfileDTO() throws Exception {

        //when + then
        mockMvc.perform(get(baseUrl).header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(user.getId()))
                .andExpect(jsonPath("username").value(user.getUsername()));
    }

    @Test
    public void should_return_user_groups_as_user_GroupListDTO() throws Exception {

        //given
        List<Game> games = bootData.createGames();
        List<Category> categories = bootData.createCategories(games);
        List<GroupRoom> groupRooms = bootData.createGroupRooms(users,categories,games,bootData.createChats());
        bootData.setGroups(users,groupRooms);
        GroupRoom groupRoom = groupRooms.get(0);

        //when + then
        mockMvc.perform(get(baseUrl + "/my-groups").header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("groupRooms[0].id").value(groupRoom.getId()))
                .andExpect(jsonPath("groupRooms[0].name").value(groupRoom.getName()));

    }

    @Test
    public void should_return_list_of_BannedUserDTO() throws Exception {
        //given
        Long bannedUserId = users.get(4).getId();
        String bannedUserUsername = "Yeager";
        int numberOfBannedUsers = 2;

        //when + then
        mockMvc.perform(get(baseUrl + "/banned").header("authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(numberOfBannedUsers)))
                .andExpect(jsonPath("[0].id").value(bannedUserId))
                .andExpect(jsonPath("[0].username").value(bannedUserUsername));
    }

    @Test
    public void should_send_friendRequest_to_user_by_id() throws Exception {
        //given
        Long userId = users.get(2).getId();

        //when + then
        mockMvc.perform(post(baseUrl + "/sendFriendRequest/" + userId)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }


    @Test
    public void should_report_user_by_userId() throws Exception {
        //given
        Long userId = users.get(2).getId();;
        ReportDTO reportDTO = getReportDTOMock();
        byte[] content = Converter.convertObjectToJsonBytes(reportDTO);

        //when + then
        mockMvc.perform(put(baseUrl + "/report/" + userId)
                        .contentType(MediaType.APPLICATION_JSON).content(content)
                        .header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}
