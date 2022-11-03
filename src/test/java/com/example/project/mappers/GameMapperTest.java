package com.example.project.mappers;

import com.example.project.domain.Game;
import com.example.project.model.GameDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.example.project.samples.GameSample.getGameMock;
import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    private final InGameRolesMapper inGameRolesMapper = Mappers.getMapper(InGameRolesMapper.class);
    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    private final GameMapper gameMapper = new GameMapperImpl(inGameRolesMapper,categoryMapper);

    @Test
    void should_map_game_to_gameDTO() {
        //given
        Game game = getGameMock();

        //when
        GameDTO result = gameMapper.mapGameToGameDTO(game);

        //then
        assertEquals(game.getId(), result.getId());
        assertEquals(game.getName(), result.getName());
    }
}