package com.example.antibullyingroleplay.enums;

import lombok.Getter;

@Getter
public enum LearningGoal {
    EMPATHY(
            "共感力の向上",
            """
                    ・全ての登場人物の視点から気持ちを考えさせてください
                    ・いじめ否定規範を高めることが目的なので、特にいじめ被害者の気持ちを想像させてください
                    """),
    PROBLEM_SOLVING(
            "問題解決能力の育成",
            """
                    ・登場人物の立場から問題解決のためにとれる行動を考えさせてください。
                    特に傍観者教育を目的として、登場人物の視点を基軸にしつつ、第三者の立場から取るべき行動を考えさせてください。
                    登場人物が2人の場合は、その状況を見ているクラスメイトとしてどのような行動が取れるかを考えさせてください
                    """),
    COMMUNICATION(
            "コミュニケーションスキルの向上",
            """
                    ・全ての登場人物の視点に立ってどのような行動を取るべきかを考えます
                    ・登場人物が対話を通じて誤解や対立を解消するための方法を考えさせてください
                    """);

    private final String description;
    private final String instruction;

    LearningGoal(String description, String instruction) {
        this.description = description;
        this.instruction = instruction;
    }

}