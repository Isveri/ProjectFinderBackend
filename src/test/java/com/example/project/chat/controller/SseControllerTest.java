package com.example.project.chat.controller;

import com.example.project.chat.service.SseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SseControllerTest {

    @Mock
    private SseService sseService;
    private MockMvc mockMvc;

    private SseController sseController;

    private final String baseUrl = "/api/v1/notify";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sseController = new SseController(sseService);
        mockMvc = MockMvcBuilders.standaloneSetup(sseController).build();

    }

    @Test
    void notifyUser() throws Exception {
        //given
        SseEmitter sseEmitter = new SseEmitter();
        when(sseService.createEmitter()).thenReturn(sseEmitter);

        //when + then
        mockMvc.perform(get(baseUrl + "/info"))
                .andExpect(status().isOk());

        verify(sseService,times(1)).createEmitter();
    }
}