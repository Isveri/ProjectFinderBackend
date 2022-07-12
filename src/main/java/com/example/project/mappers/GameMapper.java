package com.example.project.mappers;

import com.example.project.domain.Game;
import com.example.project.model.GameDTO;
import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true),
        uses = InGameRolesMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class GameMapper {

    public abstract GameDTO mapGameToGameDTO(Game game);
}
