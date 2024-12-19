package com.example.antibullyingroleplay.controller;

import com.example.antibullyingroleplay.model.DocumentData;
import com.example.antibullyingroleplay.service.GoogleDocsService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleplayController {
    private final GoogleDocsService googleDocsService;

    @Value("${document.redirect.url}")
    private String documentRedirectUrl;

    public RoleplayController(GoogleDocsService googleDocsService) {
        this.googleDocsService = googleDocsService;
    }

    @GetMapping("/auth-url")
    public ResponseEntity<Map<String, String>> createRoleplayDocument() {
        try {
            // 認証URLを取得
            String authUrl = googleDocsService.getAuthorizationUrl();
            // 認証URLを開く
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authUrl)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "認証URLの取得に失敗しました"));
        }
    }

    // リダイレクトURIで受け取った認証コードを処理するための新しいエンドポイント
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> handleCallback(@RequestParam("code") String authorizationCode) {
        try {
            // 認証コードからAccessTokenを取得
            String accessToken = googleDocsService.getAccessToken(authorizationCode);

            // フロントエンドにリダイレクトし、アクセストークンをURLパラメータとして渡す
            String redirectUrl = documentRedirectUrl + URLEncoder.encode(accessToken, "UTF-8");
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(redirectUrl))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "ドキュメントの作成に失敗しました"));
        }
    }

    @PostMapping("/create-document")
    public ResponseEntity<Map<String, String>> createRoleplayDocument(@RequestBody DocumentData documentData) {
        try {
            // ドキュメントを作成し、urlを返す
            Map<String, String> result = googleDocsService.createDocument(documentData.accessToken(), documentData.data());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "ドキュメントの作成に失敗しました"));
        }
    }
}
