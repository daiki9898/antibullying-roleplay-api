package com.example.antibullyingroleplay.service;

import com.example.antibullyingroleplay.model.RoleplayResponse;
import com.example.antibullyingroleplay.utils.TextUtils;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.List;

@Service
public class GoogleDocsService {
    private final TemplateEngine templateEngine;

    public GoogleDocsService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    // 定数
    @Value("${google.application.name}")
    private String applicationName;

    @Value("${google.credentials.file.path}")
    private String credentialsFilePath;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES =
            Collections.singletonList(DocsScopes.DOCUMENTS);
    

    // 認証URLを取得
    public String getAuthorizationUrl() throws IOException {
        InputStream in = GoogleDocsService.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new NetHttpTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        return flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
    }

    public String getAccessToken(String authorizationCode) throws IOException {
        // Load client secrets.
        InputStream in = GoogleDocsService.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JSON_FACTORY,
                clientSecrets.getDetails().getClientId(),
                clientSecrets.getDetails().getClientSecret(),
                authorizationCode,
                redirectUri)
                .execute();

        return tokenResponse.getAccessToken();
    }

    public Map<String, String> createDocument(String accessToken, RoleplayResponse roleplayResponse) throws IOException, GeneralSecurityException {
        // ThymeleafのContextにデータを設定
        Context context = new Context();
        context.setVariable("scenario", roleplayResponse.scenario());
        context.setVariable("worksheet", roleplayResponse.worksheet());

        // HTMLを生成
        String htmlContent = templateEngine.process("roleplay-template", context);
        // HTMLをプレーンテキストに変換
        String plainText = TextUtils.convertHtmlToPlainText(htmlContent);

        // AccessTokenを使ってGoogleDocsにアクセス
        GoogleCredentials credential = new GoogleCredentials(new AccessToken(accessToken, null));

        // documentを作成
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
        Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(applicationName)
                .build();

        Document doc = new Document()
                .setTitle(roleplayResponse.scenario().title());
        doc = service.documents().create(doc).execute();
        String documentId = doc.getDocumentId();

        // テキストを挿入するリクエストを作成
        InsertTextRequest insertTextRequest = new InsertTextRequest()
                .setText(plainText)
                .setLocation(new Location().setIndex(1));

        // バッチ更新リクエストを作成
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setInsertText(insertTextRequest));
        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest().setRequests(requests);

        // ドキュメントを更新
        service.documents().batchUpdate(documentId, body).execute();

        Map<String, String> result = new HashMap<>();
        result.put("document_url", "https://docs.google.com/document/d/" + documentId + "/edit");

        return result;
    }
}
