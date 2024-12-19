package com.example.antibullyingroleplay.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentData(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("data")
        RoleplayResponse data
) {
}
