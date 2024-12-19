package com.example.antibullyingroleplay.utils;

import com.example.antibullyingroleplay.enums.BullyingSeverity;
import com.example.antibullyingroleplay.enums.BullyingType;
import com.example.antibullyingroleplay.enums.Grade;
import com.example.antibullyingroleplay.enums.GradeInstruction;
import com.example.antibullyingroleplay.enums.LearningGoal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.example.antibullyingroleplay.model.RoleplayRequest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class TextUtils {
    private static String examplesPath;

    @Value("${examples.path}")
    public void setExamplesPath(String path) {
        TextUtils.examplesPath = path;
    }

    private TextUtils() {
        // ユーティリティクラスのため、インスタンス化を防ぐ
    }

    public static String loadTextFile(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
            throw new RuntimeException("テキストファイルの読み込みに失敗しました: " + path, e);
        }
    }

    private static String loadExample(String bullyingType) {
        return loadTextFile(examplesPath + bullyingType.toLowerCase() + ".txt");
    }


    public static String createPrompt(String template, RoleplayRequest request) {
        // 場面が空でない場合、条件に場面を追加
        String situationCondition = !request.bullyingSituation().isEmpty()
                ? String.format("場面：%s\n", request.bullyingSituation())
                : "";

        return template
                .replace("{GRADE_INSTRUCTION}", GradeInstruction.valueOf(request.grade()).getInstruction())
                .replace("{BULLYING_TYPE}", BullyingType.valueOf(request.bullyingType()).getDescription())
                .replace("{BULLYING_SEVERITY}", BullyingSeverity.valueOf(request.bullyingSeverity()).getDescription())
                .replace("{CHARACTER_COUNT}", String.valueOf(request.characterCount()))
                .replace("{GRADE}", Grade.valueOf(request.grade()).getDescription())
                .replace("{BULLYING_SITUATION}", situationCondition)
                .replace("{EXAMPLE}", loadExample(request.bullyingType()))
                .replace("{LEARNING_GOAL}", LearningGoal.valueOf(request.learningGoal()).getDescription())
                .replace("{LEARNING_GOAL_INSTRUCTION}", LearningGoal.valueOf(request.learningGoal()).getInstruction());
    }

    public static String convertHtmlToPlainText(String htmlContent) {
        // HTMLをパース
        Document doc = Jsoup.parse(htmlContent);

        // タイトルを処理
        String title = doc.select("h1").text();
        StringBuilder plainText = new StringBuilder();

        // ** シナリオ **//
        plainText.append("===  シナリオ  ===\n\n").append("「").append(title).append("」").append("\n");

        // 設定セクション
        doc.select("section").forEach(section -> {
            // セクションのタイトル（例: "登場人物", "状況説明"）
            String sectionTitle = section.select("h2").text();
            plainText.append("\n** ").append(sectionTitle).append(" **\n");

            // 登場人物セクションの場合
            if (sectionTitle.equals("登場人物")) {
                section.select("div.character").forEach(character -> {
                    // 登場人物の名前と説明を取得
                    String characterName = character.select("h4").text();
                    String characterDesc = character.select("p").text();
                    plainText.append("- ").append(characterName).append("：").append(characterDesc).append("\n");
                });
            }
            // 状況説明セクションの場合
            else if (sectionTitle.equals("状況説明")) {
                // 状況説明テキストを取得
                String situationDescription = section.select("p").text();
                plainText.append(situationDescription).append("\n");
            }
        });

        // シーン部分
        plainText.append("\n");
        doc.select(".scene").forEach(scene -> {
            String sceneTitle = scene.select("h3").text();
            plainText.append("\n- ").append(sceneTitle).append("\n\n");

            scene.select(".dialogue").forEach(dialogue -> {
                String speaker = dialogue.select("strong").text();
                String line = dialogue.select("span").text();
                if (!speaker.isEmpty()) {
                    plainText.append(speaker).append("：").append(line).append("\n");
                } else {
                    plainText.append("「").append(line).append(" 」\n");
                }
            });
        });

        // 後日談
        plainText.append("\n- 後日談\n");
        String postscript = doc.select(".postscript").text();
        plainText.append(postscript).append("\n");

        // ** ワークシート ** //
        plainText.append("\n\n===  ワークシート  ===\n");
        doc.select(".worksheet .question").forEach(question -> {
            String questionTitle = question.select("h3").text();
            String questionText = question.select("p").text();
            plainText.append("\n- ").append(questionTitle).append("\n").append(questionText).append("\n");
        });

        return plainText.toString();
    }
} 