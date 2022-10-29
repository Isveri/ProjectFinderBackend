package com.example.project.services;

import com.example.project.domain.Game;
import com.example.project.mappers.GameMapper;
import com.example.project.model.GameDTO;
import com.example.project.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.project.samples.GameSample.getGameDTOMock;
import static com.example.project.samples.GameSample.getGameMock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
class GameServiceImplTest {


    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    private GameServiceImpl gameService;

    @Test
    void getGames() {
        MockitoAnnotations.openMocks(this);
        gameService = new GameServiceImpl(gameRepository, gameMapper);

        //given
        Game game = getGameMock();
        GameDTO gameDTO = getGameDTOMock();
        when(gameRepository.findAll()).thenReturn(Arrays.asList(game));
        when(gameMapper.mapGameToGameDTO(any(Game.class))).thenReturn(gameDTO);

        //when
        List<GameDTO> games = gameService.getGames();

        //then
        assertThat(games.get(0).getName())
                .isEqualTo(game.getName());

        verify(gameRepository,times(1)).findAll();
        verify(gameMapper, times(1)).mapGameToGameDTO(game);
    }
}