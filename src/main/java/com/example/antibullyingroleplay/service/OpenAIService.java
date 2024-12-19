package com.example.antibullyingroleplay.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;

import com.example.antibullyingroleplay.model.RoleplayRequest;
import com.example.antibullyingroleplay.model.RoleplayResponse;
import com.example.antibullyingroleplay.utils.TextUtils;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    private final OpenAiChatModel chatModel;
    private final String promptTemplate;

    public OpenAIService(OpenAiChatModel chatModel, @Value("${prompt-template.path}") String templatePath) {
        this.chatModel = chatModel;
        this.promptTemplate = TextUtils.loadTextFile(templatePath);
    }

    public RoleplayResponse generateRoleplay(RoleplayRequest request) {
        // promptTextの作成
        String promptText = TextUtils.createPrompt(promptTemplate, request);

        // objectとjsonのマッピング
        var outputConverter = new BeanOutputConverter<>(RoleplayResponse.class);
        var jsonSchema = outputConverter.getJsonSchema();

        // promptの作成
        Prompt prompt = new Prompt(promptText,
                OpenAiChatOptions.builder()
                        .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
                        .build());

        // 実行
        ChatResponse response = chatModel.call(prompt);
        // contentを取得し、objectに変換し返す
        String content = response.getResult().getOutput().getContent();
        return outputConverter.convert(content);
    }
}
