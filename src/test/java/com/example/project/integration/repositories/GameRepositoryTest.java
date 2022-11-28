package com.example.project.integration.repositories;

import com.example.project.domain.Game;
import com.example.project.integration.bootData.BootData;
import com.example.project.repositories.GameRepository;
import com.example.project.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GameRepositoryTest {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    private BootData bootData;
    private List<Game> games;
    private Game game;

    @BeforeEach
    @Rollback
    void setUp(){
        games=bootData.createGames();
        game=games.get(0);
    }
    @Test
    void should_return_game_by_gameName(){
        //given
        String gameName=game.getName();
        //when
        Optional<Game> result=gameRepository.findByName(gameName);
        //then
        assertEquals(gameName,result.get().getName());
    }
    @Test
    void should_return_null(){
        //given
        String gameName="washing machine";
        //when
        Optional<Game> result=gameRepository.findByName(gameName);
        //then
        assertEquals(Optional.empty(),result);
    }
}
