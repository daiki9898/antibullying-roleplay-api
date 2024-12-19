package com.example.antibullyingroleplay;

import com.example.antibullyingroleplay.model.RoleplayRequest;
import com.example.antibullyingroleplay.service.OpenAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AntibullyingRoleplayApplicationTests {

    @Autowired
    private OpenAIService openAIService;

    @Test
    void testGenerateRoleplay() {
        // テスト用のリクエストを作成
        RoleplayRequest request = new RoleplayRequest(
                "EMPATHY",
                "MIDDLE",
                "VERBAL",
                "MODERATE",
                3,
                "プールの授業"
        );

        // サービスメソッドを実行
        openAIService.generateRoleplay(request);
    }

    @Test
    void contextLoads() {
    }

}
