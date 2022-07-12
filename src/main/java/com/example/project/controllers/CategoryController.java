package com.example.project.controllers;

import com.example.project.model.GameDTO;
import com.example.project.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<GameDTO>> getGames(){
        return ResponseEntity.ok(gameService.getGames());
    }
}
