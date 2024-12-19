package com.example.antibullyingroleplay.enums;

import lombok.Getter;

@Getter
public enum Grade {
    ELEMENTARY_LOW("小学校低学年", GradeInstruction.ELEMENTARY_LOW),
    ELEMENTARY_HIGH("小学校高学年", GradeInstruction.ELEMENTARY_HIGH),
    MIDDLE("中学生", GradeInstruction.MIDDLE),
    HIGH("高校生", GradeInstruction.HIGH);

    private final String description;
    private final GradeInstruction gradeInstruction;

    Grade(String description, GradeInstruction gradeInstruction) {
        this.description = description;
        this.gradeInstruction = gradeInstruction;
    }

}