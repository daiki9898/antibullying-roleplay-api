package com.example.antibullyingroleplay.enums;

import lombok.Getter;

@Getter
public enum GradeInstruction {
    ELEMENTARY_LOW(
            """
                    ・{GRADE}が共感できるリアルで具体的な内容にしてください
                    ・漢字は小学校1年生および2年生で学ぶ漢字（教育漢字）だけを使い、それ以外の漢字はひらがなで書いてください。また、語彙や表現も{GRADE}にとってわかりやすいものに限定し、長い文章は避けてください"""
    ),
    ELEMENTARY_HIGH(
            "・{GRADE}が共感できるリアルで具体的な内容にし、{GRADE}が理解できる表現を使ってください"
    ),
    MIDDLE(
            "・{GRADE}が共感できるリアルで具体的な内容にし、{GRADE}が理解できる表現を使ってください"
    ),
    HIGH(
            "・{GRADE}が共感できるリアルで具体的な内容にし、{GRADE}が理解できる表現を使ってください"
    );

    private final String instruction;

    GradeInstruction(String instruction) {
        this.instruction = instruction;
    }

}
