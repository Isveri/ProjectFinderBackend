package com.example.project.bootdata;

import com.example.project.chat.model.Chat;
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
    private final PasswordEncoder passwordEncoder;
    private final ChatRepository chatRepository;
    private final InGameRoleRepository inGameRoleRepository;
    private final GameRepository gameRepository;


    @Override
    public void run(String... args) throws Exception {

        loadData();

    }


    private void loadData() {

        List<User> users = createUsers();

        List<Chat> chats = createChats();

        List<Game> games = createGames();

        List<InGameRole> inGameRoles = createInGameRoles(games);

        List<Category> categories = createCategories(games);

        List<GroupRoom> groupRooms = createGroupRooms(users, categories, games, chats);

        setGroupsAndInGameRoles(setUserPrivilages(users), groupRooms, inGameRoles);

    }


    private List<User> createUsers() {
        User u1 = User.builder()
                .username("Evi")
                .email("evistifate1@gmail.com")
                .name("Patryk")
                .age(21)
                .city("Lublin")
                .enabled(true)
                .phone(551345345)
                .info("Challanger in every role in League of Legends. Global elite in CS:GO and Immortal in Valorant. 706gs BDO kek")
                .password(passwordEncoder.encode("admin"))
                .build();

        User u2 = User.builder()
                .username("User")
                .email("evistifate1@gmail.com")
                .name("Adam")
                .enabled(true)
                .age(21)
                .city("Lublin")
                .phone(551343223)
                .info("I like cakes")
                .password(passwordEncoder.encode("user"))
                .build();

        User u3 = User.builder()
                .username("Arthur")
                .email("evistifate1@gmail.com")
                .enabled(true)
                .password(passwordEncoder.encode("arthur"))
                .build();

        User u4 = User.builder()
                .username("William")
                .enabled(true)
                .email("evistifate1@gmail.com")
                .password(passwordEncoder.encode("william"))
                .build();

        User u5 = User.builder()
                .username("Yeager")
                .enabled(true)
                .email("evistifate1@gmail.com")
                .password(passwordEncoder.encode("yeager"))
                .accountNonLocked(false)
                .reason("Toxicity, many reports")
                .bannedBy("Evi")
                .build();

        User u6 = User.builder()
                .username("Satoru")
                .email("evistifate1@gmail.com")
                .password(passwordEncoder.encode("satoru"))
                .accountNonLocked(false)
                .enabled(true)
                .bannedBy("Evi")
                .reason("Toxicity, trolling, not taking serious warning from administration")
                .build();

        List<User> users = Arrays.asList(u1, u2, u3, u4, u5, u6);
        userRepository.saveAll(users);

        return users;
    }

    private List<Chat> createChats() {

        Chat chat1 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat2 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat3 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat4 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat5 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat6 = Chat.builder().messages(Arrays.asList()).build();
        Chat chat7 = Chat.builder().messages(Arrays.asList()).build();

        List<Chat> chats = Arrays.asList(chat1, chat2, chat3, chat4, chat5, chat6, chat7);
        chatRepository.saveAll(chats);
        return chats;
    }

    private List<InGameRole> createInGameRoles(List<Game> games) {

        InGameRole igr1 = InGameRole.builder()
                .name("Mid")
                .game(games.get(0))
                .build();

        InGameRole igr2 = InGameRole.builder()
                .name("Top")
                .game(games.get(0))
                .build();

        InGameRole igr3 = InGameRole.builder()
                .name("Bot")
                .game(games.get(0))
                .build();

        InGameRole igr4 = InGameRole.builder()
                .name("Jungle")
                .game(games.get(0))
                .build();

        InGameRole igr5 = InGameRole.builder()
                .name("Supp")
                .game(games.get(0))
                .build();


        InGameRole role1 = InGameRole.builder()
                .name("Sniper")
                .game(games.get(1))
                .build();

        InGameRole role2 = InGameRole.builder()
                .name("Support")
                .game(games.get(1))
                .build();

        InGameRole role3 = InGameRole.builder()
                .name("Leader")
                .game(games.get(1))
                .build();

        InGameRole role4 = InGameRole.builder()
                .name("Fragger")
                .game(games.get(1))
                .build();

        InGameRole role5 = InGameRole.builder()
                .name("Lurker")
                .game(games.get(1))
                .build();

        List<InGameRole> inGameRoles = Arrays.asList(igr1, igr2, igr3, igr4, igr5, role1, role2, role3, role4, role5);
        inGameRoleRepository.saveAll(inGameRoles);

        return inGameRoles;
    }

    private List<Game> createGames() {

        Game game1 = Game.builder()
                .name("League of Legends")
                .build();

        Game game2 = Game.builder()
                .name("CSGO")
                .build();

        Game game3 = Game.builder()
                .name("IRL")
                .assignRolesActive(false)
                .build();

        List<Game> games = Arrays.asList(game1, game2, game3);
        gameRepository.saveAll(games);

        return games;
    }

    private List<Category> createCategories(List<Game> games) {

        Category cat1 = Category.builder()
                .name("SoloQ")
                .game(games.get(0))
                .basicMaxUsers(2)
                .canAssignRoles(true)
                .build();

        Category cat2 = Category.builder()
                .name("Ranked Flex")
                .game(games.get(0))
                .basicMaxUsers(5)
                .canAssignRoles(true)
                .build();

        Category cat3 = Category.builder()
                .name("ARAM")
                .game(games.get(0))
                .canAssignRoles(false)
                .basicMaxUsers(5)
                .build();

        Category cat4 = Category.builder()
                .name("Custom")
                .game(games.get(0))
                .canAssignRoles(true)
                .basicMaxUsers(5)
                .build();

        Category cat5 = Category.builder()
                .name("Match Making")
                .game(games.get(1))
                .canAssignRoles(true)
                .basicMaxUsers(5).build();

        Category cat6 = Category.builder()
                .name("WingMan")
                .canAssignRoles(false)
                .game(games.get(1))
                .basicMaxUsers(2).build();

        Category cat7 = Category.builder()
                .name("FaceIt")
                .canAssignRoles(true)
                .game(games.get(1))
                .basicMaxUsers(5).build();

        Category cat8 = Category.builder()
                .name("Death Match")
                .game(games.get(1))
                .canAssignRoles(false)
                .basicMaxUsers(5).build();

        Category cat9 = Category.builder()
                .name("Cinema")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat10 = Category.builder()
                .name("Billiards")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat12 = Category.builder()
                .name("Bowls")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat13 = Category.builder()
                .name("Football")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat14 = Category.builder()
                .name("Volleyball")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat15 = Category.builder()
                .name("Basketball")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        Category cat16 = Category.builder()
                .name("Other")
                .canAssignRoles(false)
                .game(games.get(2))
                .basicMaxUsers(3)
                .build();

        List<Category> categories = Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, cat9, cat10, cat12, cat13, cat14, cat15, cat16);
        categoryRepository.saveAll(categories);

        return categories;
    }

    private List<GroupRoom> createGroupRooms(List<User> users, List<Category> categories, List<Game> games, List<Chat> chats) {
        GroupRoom g1 = GroupRoom.builder()
                .name("Grupa 1")
                .description("Poszukuje osób do wspólnego pogrania w Lolka")
                .users(new ArrayList<>(categories.get(0).getBasicMaxUsers()))
                .maxUsers(categories.get(0).getBasicMaxUsers())
                .category(categories.get(0))
                .chat(chats.get(0))
                .game(games.get(0))
                .groupLeader(users.get(0))
                .build();


        GroupRoom g2 = GroupRoom.builder()
                .name("Grupa 2")
                .description("Poszukuje osób do wspólnego pogrania w League of Legends. Wymagana ranga gold+")
                .users(new ArrayList<>(categories.get(3).getBasicMaxUsers()))
                .chat(chats.get(1))
                .groupLeader(users.get(1))
                .category(categories.get(3))
                .maxUsers(categories.get(3).getBasicMaxUsers())
                .game(games.get(0))
                .build();

        GroupRoom g3 = GroupRoom.builder()
                .name("Grupa 3")
                .users(new ArrayList<>(categories.get(1).getBasicMaxUsers()))
                .groupLeader(users.get(2))
                .description("Poszukuje osób do wspólnego pogrania w League of Legends. Wymagana ranga gold+")
                .game(games.get(0))
                .chat(chats.get(2))
                .category(categories.get(1))
                .maxUsers(categories.get(1).getBasicMaxUsers())
                .build();

        GroupRoom g4 = GroupRoom.builder()
                .name("Grupa 4")
                .users(new ArrayList<>(categories.get(2).getBasicMaxUsers()))
                .description("Poszukuje osób do wspólnego pogrania w League of Legends. Wymagana ranga gold+")
                .groupLeader(users.get(1))
                .game(games.get(0))
                .chat(chats.get(3))
                .category(categories.get(2))
                .maxUsers(categories.get(2).getBasicMaxUsers())
                .build();

        GroupRoom g5 = GroupRoom.builder()
                .name("Grupa 5")
                .users(new ArrayList<>(categories.get(4).getBasicMaxUsers()))
                .description("CSGO - Supreme+")
                .game(games.get(1))
                .chat(chats.get(4))
                .category(categories.get(4))
                .maxUsers(categories.get(4).getBasicMaxUsers())
                .groupLeader(users.get(1))
                .build();

        GroupRoom g6 = GroupRoom.builder()
                .name("Grupa 6")
                .users(new ArrayList<>(categories.get(5).getBasicMaxUsers()))
                .description("CSGO friendly chill playing ")
                .game(games.get(1))
                .chat(chats.get(5))
                .category(categories.get(5))
                .maxUsers(categories.get(5).getBasicMaxUsers())
                .groupLeader(users.get(2))
                .build();

        GroupRoom g7 = GroupRoom.builder()
                .name("Grupa 7")
                .description("Szukam osób do wyjscia na miasto.")
                .groupLeader(users.get(2))
                .game(games.get(2))
                .category(categories.get(11))
                .maxUsers(categories.get(11).getBasicMaxUsers())
                .city("Lublin")
                .chat(chats.get(6))
                .build();

        List<GroupRoom> groupRooms = Arrays.asList(g1, g2, g3, g4, g5, g6, g7);
        groupRepository.saveAll(groupRooms);


        int i = 0;
        for (Chat chat : chats) {
            chat.setGroupRoom(groupRooms.get(i));
            i++;
        }

        chatRepository.saveAll(chats);
        return groupRooms;
    }

    private List<User> setUserPrivilages(List<User> users) {
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        users.get(0).setRole(adminRole);
        users.get(1).setRole(userRole);
        users.get(2).setRole(userRole);
        users.get(3).setRole(userRole);
        users.get(4).setRole(userRole);
        users.get(5).setRole(userRole);

        return users;
    }

    private void setGroupsAndInGameRoles(List<User> users, List<GroupRoom> groupRooms, List<InGameRole> inGameRoles) {
        users.get(0).setGroupRooms(Arrays.asList(groupRooms.get(0), groupRooms.get(1), groupRooms.get(2)));
        users.get(1).setGroupRooms(Arrays.asList(groupRooms.get(0), groupRooms.get(1)));
        users.get(2).setGroupRooms(Arrays.asList(groupRooms.get(2), groupRooms.get(1), groupRooms.get(3), groupRooms.get(4)));
        users.get(3).setGroupRooms(Arrays.asList(groupRooms.get(1), groupRooms.get(2), groupRooms.get(3)));
        users.get(4).setGroupRooms(Arrays.asList(groupRooms.get(6), groupRooms.get(1), groupRooms.get(4), groupRooms.get(3)));
        users.get(5).setGroupRooms(Arrays.asList(groupRooms.get(6), groupRooms.get(5), groupRooms.get(3)));


        users.get(0).setInGameRoles(Arrays.asList(inGameRoles.get(0), inGameRoles.get(2), inGameRoles.get(6), inGameRoles.get(5), inGameRoles.get(7), inGameRoles.get(8)));
        users.get(1).setInGameRoles(Arrays.asList(inGameRoles.get(1), inGameRoles.get(2), inGameRoles.get(6)));
        users.get(2).setInGameRoles(Arrays.asList(inGameRoles.get(2), inGameRoles.get(6)));
        users.get(3).setInGameRoles(Arrays.asList(inGameRoles.get(3), inGameRoles.get(7), inGameRoles.get(8)));
        users.get(4).setInGameRoles(Arrays.asList(inGameRoles.get(4), inGameRoles.get(5), inGameRoles.get(8)));
        users.get(5).setInGameRoles(Arrays.asList(inGameRoles.get(0), inGameRoles.get(1), inGameRoles.get(6)));

        userRepository.saveAll(users);

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
