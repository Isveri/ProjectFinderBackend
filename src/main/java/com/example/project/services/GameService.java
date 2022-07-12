package com.example.project.services;

import com.example.project.model.GameDTO;
import com.example.project.model.auth.TokenResponse;

import java.util.List;

public interface GameService {
    List<GameDTO> getGames();
}
