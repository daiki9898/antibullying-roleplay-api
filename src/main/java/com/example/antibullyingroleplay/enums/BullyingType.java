package com.example.antibullyingroleplay.enums;

import lombok.Getter;

@Getter
public enum BullyingType {
    VERBAL("冷やかしやからかい、悪口や脅し文句、嫌なことを言われる"),
    EXCLUSION("仲間はずれ、集団による無視をされる"),
    PHYSICAL("軽くぶつかられたり、遊ぶふりをして叩かれたり、蹴られたりする"),
    EXTORTION("金品をたかられる"),
    PROPERTY("金品を隠されたり、盗まれたり、壊されたり、捨てられたりする"),
    COMPULSION("嫌なことや恥ずかしいこと、危険なことをされたり、させられたりする"),
    CYBER("パソコンや携帯電話等で、誹謗中傷や嫌なことをされる"),
    OTHER("その他");

    private final String description;

    BullyingType(String description) {
        this.description = description;
    }

}