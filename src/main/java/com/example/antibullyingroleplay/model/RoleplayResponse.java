package com.example.antibullyingroleplay.model;
        
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RoleplayResponse(@JsonProperty(required = true, value = "scenario") Scenario scenario,
                               @JsonProperty(required = true, value = "worksheet") Worksheet worksheet) {
    public record Scenario(
            @JsonProperty(required = true, value = "title") String title,
            @JsonProperty(required = true, value = "setting") Setting setting,
            @JsonProperty(required = true, value = "scenes") List<Scene> scenes,
            @JsonProperty(required = true, value = "postscript") String postscript
    ) {
    }

    public record Setting(
            @JsonProperty(required = true, value = "characters") List<Character> characters,
            @JsonProperty(required = true, value = "description") String description
    ) {
    }

    public record Character(
            @JsonProperty(required = true, value = "label") String label,
            @JsonProperty(required = true, value = "description") String description
    ) {
    }

    public record Scene(
            @JsonProperty(required = true, value = "scene_number") int sceneNumber,
            @JsonProperty(required = true, value = "scene_description") String sceneDescription,
            @JsonProperty(required = true, value = "dialogue_pairs") List<DialoguePair> dialoguePairs
    ) {
    }

    public record DialoguePair(
            @JsonProperty(required = true, value = "speaker") String speaker,
            @JsonProperty(required = true, value = "line") String line
//            @JsonProperty(required = false, value = "description") @JsonInclude(JsonInclude.Include.NON_NULL) String description
    ) {
    }

    public record Worksheet(
            @JsonProperty(required = true, value = "questions") List<Question> questions
    ) {
    }

    public record Question(
            @JsonProperty(required = true, value = "question_number") int questionNumber,
            @JsonProperty(required = true, value = "question_text") String questionText
    ) {
    }
}
