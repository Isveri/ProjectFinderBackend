package com.example.project.controllers;

import com.example.project.model.PlatformDTO;
import com.example.project.model.UserMsgDTO;
import com.example.project.services.PlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/platform")
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping("/connectDC")
    public ResponseEntity<PlatformDTO> connectDC(@RequestParam("accessToken") String accessToken , @RequestParam("tokenType") String tokenType){
        return ResponseEntity.ok(platformService.connectDC(accessToken, tokenType));
    }
}
