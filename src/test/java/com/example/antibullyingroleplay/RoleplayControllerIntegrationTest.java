package com.example.antibullyingroleplay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoleplayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createRoleplayDocument_Integration() throws Exception {
        // テスト用のJSONリクエスト
        String jsonRequest = """
                {
                  "scenario": {
                    "title": "心に残る冗談",
                    "setting": {
                      "characters": [
                        {
                          "label": "A",
                          "description": "中学2年生。絵を描くことが好きでおとなしい性格。"
                        }
                      ],
                      "description": "Aは絵を描くことが好きな中学生で..."
                    },
                    "scenes": [],
                    "postscript": "AはBの冗談に少し傷ついていたが..."
                  },
                  "worksheet": {
                    "questions": []
                  }
                }
                """;

        mockMvc.perform(post("/create-document")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print()) // レスポンス内容を出力
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentUrl").exists()); // 実際のドキュメントIDが返るかを確認
    }
}
