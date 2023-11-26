package com.BF2042Stats.data.data_enum;

import java.awt.*;

public enum ClassesType {
    Engineer("\u5de5\u7a0b\u5175",new Color( 0, 75, 80)),
    Support("\u652f\u63f4\u5175",new Color(0, 178, 148)),
    Antitank("\u53cd\u5766\u514b",new Color( 234, 185, 138)),
    Assault("\u571f\u9e21\u5175",new Color( 234, 185, 138)),
    Medic("\u4eba\u95f4\u5929\u4f7f",new Color(0, 178, 148)),
    Recon("\u659f\u8336\u5175",new Color(180, 160, 255)),
    Scout("\u659f\u8336\u5458",new Color(180, 160, 255));
    private final String s1;
    private final Color color;

    ClassesType(String s1,Color color) {
        this.s1 = s1;
        this.color = color;
    }

    public String getS1() {
        return s1;
    }

    public Color getColor() {
        return color;
    }
}
