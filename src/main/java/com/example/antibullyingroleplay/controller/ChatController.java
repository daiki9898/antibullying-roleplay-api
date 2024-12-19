package com.example.antibullyingroleplay.controller;

import com.example.antibullyingroleplay.model.RoleplayRequest;
import com.example.antibullyingroleplay.model.RoleplayResponse;
import com.example.antibullyingroleplay.service.OpenAIService;

import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {
    private final OpenAIService openAIService;

    public ChatController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("generate-roleplay")
    public RoleplayResponse generateRoleplay(@RequestBody RoleplayRequest request) {
        return openAIService.generateRoleplay(request);
    }
}
