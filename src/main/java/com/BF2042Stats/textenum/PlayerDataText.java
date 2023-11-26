package com.BF2042Stats.textenum;

public enum PlayerDataText {
    B1("前面的薯条，以后在来捞吧"),
    B2("想要家人都吃饱饭吗？"),
    B3("感觉不如12M"),
    B4("兵者，诡道也"),
    B5("百战百胜非善之善者也"),
    B6("知己知彼，百战不殆"),
    B7("胜者为王，败者为寇"),
    B8("把他们上市！"),
    B9("芜湖~我的表现真是可圈可点呐"),
    E1("要不咱们去玩原神吧？"),
    E2("玩2042玩的"),
    W12("孩子别怕，我拿12m来救你了"),
    V1("我靠你驾照是不是买的？");
    final String text;

    PlayerDataText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
