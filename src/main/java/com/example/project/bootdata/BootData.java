package com.example.project.bootdata;

import com.example.project.domain.*;
import com.example.project.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    private void createOther(){
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

        Message c1 = Message.builder()
                .text("To jest moj pierwszy komentarz tutaj")
                .user(u1)
                .build();


        Message c2 = Message.builder()
                .text("To jest moj pierwszy komentarz tutaj")
                .user(u2)
                .build();

        Message c3 = Message.builder()
                .text("To jest moj drugi komentarz tutaj")
                .user(u1)
                .build();

        Message c4 = Message.builder()
                .text("To jest moj trzeci komentarz tutaj")
                .user(u1)
                .build();

        Chat ch1 = Chat.builder().build();
        Chat ch2 = Chat.builder().build();
        Chat ch3 = Chat.builder().build();
        Chat ch4 = Chat.builder().build();
        Chat ch5 = Chat.builder().build();
        Chat ch6 = Chat.builder().build();
        Chat ch7 = Chat.builder().build();

        chatRepository.save(ch1);
        chatRepository.save(ch2);
        chatRepository.save(ch3);
        chatRepository.save(ch4);
        chatRepository.save(ch5);
        chatRepository.save(ch6);
        chatRepository.save(ch7);


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
        InGameRole role2 = InGameRole.builder().name("Entry fragger").build();
        InGameRole role3 = InGameRole.builder().name("Shoot Caller").build();

        inGameRoleRepository.save(role1);
        inGameRoleRepository.save(role2);
        inGameRoleRepository.save(role3);

        Game game1 = Game.builder().name("League of Legends").inGameRoles(Arrays.asList(igr1,igr2,igr3,igr4,igr5)).build();
        Game game2 = Game.builder().name("CSGO").inGameRoles(Arrays.asList(igr1,igr2,igr3,igr4,igr5)).build();
        Game game3 = Game.builder().name("IRL").build();

        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(game3);


        Category cat1 = Category.builder()
                .name("SoloQ")
                .game(game1)
                .build();
        Category cat2 = Category.builder()
                .name("Ranked Flex")
                .game(game1)
                .build();
        Category cat3 = Category.builder()
                .name("ARAM")
                .game(game1)
                .build();
        Category cat4 = Category.builder()
                .name("Nieokreślone")
                .game(game1)
                .build();


        categoryRepository.save(cat1);
        categoryRepository.save(cat2);
        categoryRepository.save(cat3);
        categoryRepository.save(cat4);



        GroupRoom g1 =GroupRoom.builder()
                .name("Grupa 1")
                .description("Poszukuje osób do wspólnego pogrania w CS:GO")
                .users(Collections.singletonList(u1))
                .chat(ch1)
                .category(cat1)
                .game(game1)
                .groupLeader(u1)
                .build();

        groupRepository.save(g1);

        GroupRoom g2 = GroupRoom.builder()
                .name("Grupa 2")
                .description("Poszukuje osób do wspólnego pogrania w League of Legends. Wymagana ranga gold+")
                .users(Collections.singletonList(u1))
                .groupLeader(u2)
                .category(cat1)
                .chat(ch2)
                .game(game1)
                .build();

        groupRepository.save(g2);
        GroupRoom g3 = GroupRoom.builder()
                .name("Grupa 3")
                .users(Collections.singletonList(u1))
                .groupLeader(u3)
                .description("Poszukuje osób do wspólnego wyjścia na kręgle ")
                .chat(ch3)
                .game(game1)
                .category(cat1)
                .build();
        groupRepository.save(g3);

        GroupRoom g4 = GroupRoom.builder()
                .name("Grupa 4")
                .users(Collections.singletonList(u1))
                .description("Poszukuje osób do wspólnego wyjścia na mecz koszykówki")
                .chat(ch4)
                .groupLeader(u2)
                .game(game1)
                .category(cat2)
                .build();
        groupRepository.save(g4);

        GroupRoom g5 = GroupRoom.builder()
                .name("Grupa 5")
                .users(Collections.singletonList(u1))
                .description("Grupa do rozmowy na różne tematy")
                .chat(ch5)
                .game(game2)
                .groupLeader(u2)
                .build();
        groupRepository.save(g5);

        GroupRoom g6 = GroupRoom.builder()
                .name("Grupa 6")
                .users(Collections.singletonList(u1))
                .description("Poszukuje osób zainteresowanych programowaniem i pracą w zespole")
                .chat(ch6)
                .game(game2)
                .groupLeader(u3)
                .build();
        groupRepository.save(g6);

        GroupRoom g7 = GroupRoom.builder()
                .name("Grupa 7")
                .users(Collections.singletonList(u1))
                .description("Szukam osób do wspólnego pogrania w League of Legends. Ranga plat+")
                .chat(ch7)
                .groupLeader(u3)
                .game(game3)
                .build();
        groupRepository.save(g7);

        ch1.setGroupRoom(g1);
        ch2.setGroupRoom(g2);
        ch3.setGroupRoom(g3);
        ch4.setGroupRoom(g4);
        ch5.setGroupRoom(g5);
        ch6.setGroupRoom(g6);
        ch7.setGroupRoom(g7);

        chatRepository.save(ch1);
        chatRepository.save(ch2);
        chatRepository.save(ch3);
        chatRepository.save(ch4);
        chatRepository.save(ch5);
        chatRepository.save(ch6);
        chatRepository.save(ch7);

        c1.setChat(g2.getChat());
        c2.setChat(g3.getChat());
        c3.setChat(g1.getChat());
        c4.setChat(g4.getChat());

        messageRepository.save(c1);
        messageRepository.save(c2);
        messageRepository.save(c3);
        messageRepository.save(c4);

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");



        u1.setGroupRooms(Arrays.asList(g1,g2,g3));
        u2.setGroupRooms(Arrays.asList(g1,g3));
        u3.setGroupRooms(Arrays.asList(g1,g2,g3,g4));
        u4.setGroupRooms(Arrays.asList(g1,g2,g3,g4));
        u5.setGroupRooms(Arrays.asList(g7,g2,g5,g4));
        u6.setGroupRooms(Arrays.asList(g7,g2,g6,g4));

        u1.setRole(adminRole);
        u2.setRole(userRole);
        u3.setRole(userRole);
        u4.setRole(userRole);
        u5.setRole(userRole);
        u6.setRole(userRole);
        adminRole.setUsers(Arrays.asList(u1));
        userRole.setUsers(Arrays.asList(u2,u3,u4,u5,u6));

        u1.setInGameRoles(Arrays.asList(igr1,igr3));
        u2.setInGameRoles(Arrays.asList(igr2,igr4));
        u3.setInGameRoles(Arrays.asList(igr3));
        u4.setInGameRoles(Arrays.asList(igr4));
        u5.setInGameRoles(Arrays.asList(igr5));
        u6.setInGameRoles(Arrays.asList(igr1,igr2));

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
