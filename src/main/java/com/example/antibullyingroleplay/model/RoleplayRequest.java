package com.example.antibullyingroleplay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoleplayRequest(
        @JsonProperty("learning_goal") String learningGoal,
        @JsonProperty("grade") String grade,
        @JsonProperty("bullying_type") String bullyingType,
        @JsonProperty("bullying_severity") String bullyingSeverity,
        @JsonProperty("character_count") int characterCount,
        @JsonProperty("bullying_situation") String bullyingSituation
) {
}
