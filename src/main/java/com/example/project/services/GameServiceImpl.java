package com.example.project.services;

import com.example.project.mappers.GameMapper;
import com.example.project.model.GameDTO;
import com.example.project.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    @Override
    public List<GameDTO> getGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::mapGameToGameDTO)
                .collect(Collectors.toList());
    }
}
