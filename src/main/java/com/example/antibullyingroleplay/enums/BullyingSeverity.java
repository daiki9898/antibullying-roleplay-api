package com.example.antibullyingroleplay.enums;

import lombok.Getter;

@Getter
public enum BullyingSeverity {
    MILD("軽度"),
    MODERATE("中度"),
    SEVERE("重度（重大事態2号）"),
    CRITICAL("重度（重大事態1号）");

    private final String description;

    BullyingSeverity(String description) {
        this.description = description;
    }

}