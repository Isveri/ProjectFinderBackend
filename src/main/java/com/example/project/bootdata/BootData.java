package com.example.project.bootdata;

import com.example.project.chat.model.Chat;
import com.example.project.chat.model.Message;
import com.example.project.chat.repositories.ChatRepository;
import com.example.project.chat.repositories.MessageRepository;
import com.example.project.domain.*;
import com.example.project.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BootData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private final PrivilegesRepository privilegesRepository;
    private final RoleRepository roleRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder;

    private final ChatRepository chatRepository;

    private final InGameRoleRepository inGameRoleRepository;

    private final GameRepository gameRepository;


    @Override
    public void run(String... args) throws Exception {


//        createCategory();
        createOther();


    }

//    private void createCategory() {
//
//        categoryRepository.save(Category.builder()
//                .name("SoloQ")
//                .build());
//
//        categoryRepository.save(Category.builder()
//                .name("Ranked Flex")
//                .build());
//
//        categoryRepository.save(Category.builder()
//                .name("ARAM")
//                .build());
//
//        categoryRepository.save(Category.builder()
//                .name("Nieokreślone")
//                .build());
//
//    }

    private void createOther() {
        User u1 = User.builder()
                .username("Evi")
                .email("evi@gmail.com")
                .name("Patryk")
                .age(21)
                .city("Lublin")
                .phone(551345345)
                .info("Challanger in every role in League of Legends. Global elite in CS:GO and Immortal in Valorant. 706gs BDO kek")
                .password(passwordEncoder.encode("admin"))
                .build();

        User u2 = User.builder()
                .username("User")
                .email("user@gmail.com")
                .name("Adam")
                .age(21)
                .city("Lublin")
                .phone(551343223)
                .info("I like cakes")
                .password(passwordEncoder.encode("user"))
                .build();

        User u3 = User.builder()
                .username("Arthur")
                .email("arthur@gmail.com")
                .password(passwordEncoder.encode("arthur"))
                .build();

        User u4 = User.builder()
                .username("William")
                .email("william@gmail.com")
                .password(passwordEncoder.encode("william"))
                .build();

        User u5 = User.builder()
                .username("Yeager")
                .email("yeager@gmail.com")
                .password(passwordEncoder.encode("yeager"))
                .build();

        User u6 = User.builder()
                .username("Satoru")
                .email("satoru@gmail.com")
                .password(passwordEncoder.encode("satoru"))
                .build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);
        userRepository.save(u5);
        userRepository.save(u6);

        Chat chat1 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat2 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat3 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat4 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat5 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat6 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat7 = Chat.builder().messages(Arrays.asList()).build();

        chatRepository.save(chat7);
        chatRepository.save(chat6);
        chatRepository.save(chat5);
        chatRepository.save(chat4);
        chatRepository.save(chat3);
        chatRepository.save(chat2);
        chatRepository.save(chat1);


        InGameRole igr1 = InGameRole.builder().name("Mid").build();
        InGameRole igr2 = InGameRole.builder().name("Top").build();
        InGameRole igr3 = InGameRole.builder().name("Bot").build();
        InGameRole igr4 = InGameRole.builder().name("Jungle").build();
        InGameRole igr5 = InGameRole.builder().name("Supp").build();

        inGameRoleRepository.save(igr1);
        inGameRoleRepository.save(igr2);
        inGameRoleRepository.save(igr3);
        inGameRoleRepository.save(igr4);
        inGameRoleRepository.save(igr5);


        InGameRole role1 = InGameRole.builder().name("Sniper").build();
        InGameRole role2 = InGameRole.builder().name("Support").build();
        InGameRole role3 = InGameRole.builder().name("Leader").build();
        InGameRole role4 = InGameRole.builder().name("Fragger").build();

        inGameRoleRepository.save(role1);
        inGameRoleRepository.save(role2);
        inGameRoleRepository.save(role3);
        inGameRoleRepository.save(role4);



        Game game1 = Game.builder().name("League of Legends").inGameRoles(Arrays.asList(igr1, igr2, igr3, igr4, igr5)).build();
        Game game2 = Game.builder().name("CSGO").inGameRoles(Arrays.asList(role1, role2, role3,role4)).build();
        Game game3 = Game.builder().name("IRL").build();

        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(game3);

        igr1.setGame(game1);
        igr2.setGame(game1);
        igr3.setGame(game1);
        igr4.setGame(game1);
        igr5.setGame(game1);

        role1.setGame(game2);
        role2.setGame(game2);
        role3.setGame(game2);
        role4.setGame(game2);

        inGameRoleRepository.save(igr1);
        inGameRoleRepository.save(igr2);
        inGameRoleRepository.save(igr3);
        inGameRoleRepository.save(igr4);
        inGameRoleRepository.save(igr5);
        inGameRoleRepository.save(role1);
        inGameRoleRepository.save(role2);
        inGameRoleRepository.save(role3);
        inGameRoleRepository.save(role4);


        Category cat1 = Category.builder()
                .name("SoloQ")
                .game(game1)
                .basicMaxUsers(2)
                .build();
        Category cat2 = Category.builder()
                .name("Ranked Flex")
                .game(game1)
                .basicMaxUsers(5)
                .build();
        Category cat3 = Category.builder()
                .name("ARAM")
                .game(game1)
                .basicMaxUsers(5)
                .build();
        Category cat4 = Category.builder()
                .name("Custom")
                .game(game1)
                .basicMaxUsers(5)
                .build();

        Category cat5 = Category.builder()
                .name("Match Making")
                .game(game2)
                .basicMaxUsers(5).build();

        Category cat6 = Category.builder()
                .name("WingMan")
                .game(game2)
                .basicMaxUsers(2).build();

        Category cat7 = Category.builder()
                .name("FaceIt")
                .game(game2)
                .basicMaxUsers(5).build();

        Category cat8 = Category.builder()
                .name("Death Match")
                .game(game2)
                .basicMaxUsers(5).build();

        categoryRepository.save(cat1);
        categoryRepository.save(cat2);
        categoryRepository.save(cat3);
        categoryRepository.save(cat4);
        categoryRepository.save(cat5);
        categoryRepository.save(cat6);
        categoryRepository.save(cat7);
        categoryRepository.save(cat8);


        GroupRoom g1 = GroupRoom.builder()
                .name("Grupa 1")
                .description("Poszukuje osób do wspólnego pogrania w CS:GO")
                .users(new ArrayList<>(cat1.getBasicMaxUsers()))
                .maxUsers(cat1.getBasicMaxUsers())
                .category(cat1)
                .chat(chat1)
                .game(game1)
                .groupLeader(u1)
                .build();

        groupRepository.save(g1);

        GroupRoom g2 = GroupRoom.builder()
                .name("Grupa 2")
                .description("Poszukuje osób do wspólnego pogrania w League of Legends. Wymagana ranga gold+")
                .users(new ArrayList<>(cat4.getBasicMaxUsers()))
                .chat(chat2)
                .groupLeader(u2)
                .category(cat4)
                .maxUsers(cat4.getBasicMaxUsers())
                .game(game1)
                .build();

        groupRepository.save(g2);
        GroupRoom g3 = GroupRoom.builder()
                .name("Grupa 3")
                .users(new ArrayList<>(cat2.getBasicMaxUsers()))
                .groupLeader(u3)
                .description("Poszukuje osób do wspólnego wyjścia na kręgle ")
                .game(game1)
                .chat(chat3)
                .category(cat2)
                .maxUsers(cat2.getBasicMaxUsers())
                .build();
        groupRepository.save(g3);

        GroupRoom g4 = GroupRoom.builder()
                .name("Grupa 4")
                .users(new ArrayList<>(cat3.getBasicMaxUsers()))
                .description("Poszukuje osób do wspólnego wyjścia na mecz koszykówki")
                .groupLeader(u2)
                .game(game1)
                .chat(chat4)
                .category(cat3)
                .maxUsers(cat3.getBasicMaxUsers())
                .build();
        groupRepository.save(g4);

        GroupRoom g5 = GroupRoom.builder()
                .name("Grupa 5")
                .users(Collections.singletonList(u1))
                .description("Grupa do rozmowy na różne tematy")
                .game(game2)
                .chat(chat5)
                .category(cat5)
                .maxUsers(cat5.getBasicMaxUsers())
                .groupLeader(u2)
                .build();
        groupRepository.save(g5);

        GroupRoom g6 = GroupRoom.builder()
                .name("Grupa 6")
                .users(Collections.singletonList(u1))
                .description("Poszukuje osób zainteresowanych programowaniem i pracą w zespole")
                .game(game2)
                .chat(chat6)
                .category(cat6)
                .maxUsers(cat6.getBasicMaxUsers())
                .groupLeader(u3)
                .build();
        groupRepository.save(g6);

        GroupRoom g7 = GroupRoom.builder()
                .name("Grupa 7")
                .users(Collections.singletonList(u1))
                .description("Szukam osób do wspólnego pogrania w League of Legends. Ranga plat+")
                .groupLeader(u3)
                .category(cat8)
                .maxUsers(cat8.getBasicMaxUsers())
                .game(game3)
                .chat(chat7)
                .build();
        groupRepository.save(g7);


        chat1.setGroupRoom(g1);
        chat2.setGroupRoom(g2);
        chat3.setGroupRoom(g3);
        chat4.setGroupRoom(g4);
        chat5.setGroupRoom(g5);
        chat6.setGroupRoom(g6);
        chat7.setGroupRoom(g7);

        chatRepository.save(chat1);
        chatRepository.save(chat2);
        chatRepository.save(chat3);
        chatRepository.save(chat4);
        chatRepository.save(chat5);
        chatRepository.save(chat6);
        chatRepository.save(chat7);

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");


        u1.setGroupRooms(Arrays.asList(g1, g2, g3));
        u2.setGroupRooms(Arrays.asList(g1, g2));
        u3.setGroupRooms(Arrays.asList(g3, g2, g4, g5));
        u4.setGroupRooms(Arrays.asList(g2, g3, g4));
        u5.setGroupRooms(Arrays.asList(g7, g2, g5, g4));
        u6.setGroupRooms(Arrays.asList(g7, g6, g4));

        u1.setRole(adminRole);
        u2.setRole(userRole);
        u3.setRole(userRole);
        u4.setRole(userRole);
        u5.setRole(userRole);
        u6.setRole(userRole);
        adminRole.setUsers(Arrays.asList(u1));
        userRole.setUsers(Arrays.asList(u2, u3, u4, u5, u6));

        u1.setInGameRoles(Arrays.asList(igr1, igr3, role2, role1, role3,role4));
        u2.setInGameRoles(Arrays.asList(igr2, igr4, role2));
        u3.setInGameRoles(Arrays.asList(igr3, role2));
        u4.setInGameRoles(Arrays.asList(igr4,role3,role4));
        u5.setInGameRoles(Arrays.asList(igr5,role1,role4));
        u6.setInGameRoles(Arrays.asList(igr1, igr2,role2));

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);
        userRepository.save(u5);
        userRepository.save(u6);

//        game1.setGroupRooms(Arrays.asList(g1,g2,g3,g4));
//        game2.setGroupRooms(Arrays.asList(g5, g6));
//        game3.setGroupRooms(Arrays.asList(g7));
//
//        gameRepository.save(game1);
//        gameRepository.save(game2);
//        gameRepository.save(game3);


    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegesRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegesRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
