package com.example.antibullyingroleplay;

import com.example.antibullyingroleplay.controller.RoleplayController;
import com.example.antibullyingroleplay.model.RoleplayResponse;
import com.example.antibullyingroleplay.service.GoogleDocsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleplayController.class)
class RoleplayControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleDocsService googleDocsService;

    // Sample JSON input
    String jsonRequest = """
            {
              "scenario": {
                "title": "心に残る冗談",
                "setting": {
                  "characters": [
                    {
                      "label": "A",
                      "description": "中学2年生。絵を描くことが好きでおとなしい性格。"
                    },
                    {
                      "label": "B",
                      "description": "Aのクラスメイト。明るく社交的だが時に言葉が行き過ぎる。"
                    }
                  ],
                  "description": "Aは絵を描くことが好きな中学生で、クラスではあまり目立たない存在..."
                },
                "scenes": [
                  {
                    "scene_number": 1,
                    "scene_description": "休み時間の教室",
                    "dialogue_pairs": [
                      { "speaker": "", "line": "Aは自分の席でスケッチブックに絵を描いている。" },
                      { "speaker": "B", "line": "お、また絵描いてるの？アーティストさん！" }
                    ]
                  }
                ],
                "postscript": "AはBの冗談に少し傷ついていたが..."
              },
              "worksheet": {
                "questions": [
                  {
                    "question_number": 1,
                    "question_text": "AはBから「アーティストさん！」と言われたとき、どのように感じたでしょうか？"
                  }
                ]
              }
            }
            """;

    @Test
    void createRoleplayDocument_Success() throws Exception {
        // Mock the service response
        when(googleDocsService.createDocument(anyString(), any(RoleplayResponse.class)))
                .thenReturn(Map.of("documentId", "12345"));

        // Perform the POST request
        mockMvc.perform(post("/create-document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentId").value("12345"));
    }

    @Test
    void createRoleplayDocument_Failure() throws Exception {
        // Mock the service response to throw an exception
        when(googleDocsService.createDocument(anyString(), any(RoleplayResponse.class)))
                .thenThrow(new RuntimeException("ドキュメント作成に失敗しました"));

        // Perform the POST request
        mockMvc.perform(post("/create-document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("ドキュメントの作成に失敗しました"));
    }
}
